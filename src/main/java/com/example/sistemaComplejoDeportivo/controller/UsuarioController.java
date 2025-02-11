package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    private PasswordEncoder passwordEncoder;

    // 📌 Ver lista de usuarios (accesible para todos)
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarios"; // Muestra la lista de usuarios en usuarios.html
    }

    // 📌 Cargar formulario de nuevo usuario (SOLO ADMINISTRADOR)
    @GetMapping("/nuevo")
    public String formularioNuevoUsuario(Model model, RedirectAttributes redirectAttributes) {
        if (!esAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para agregar usuarios.");
            return "redirect:/usuarios";
        }
        model.addAttribute("usuario", new Usuario());
        return "crear-usuarios"; // Vista del formulario de nuevo usuario
    }

    // 📌 Crear un nuevo usuario (SOLO ADMINISTRADOR)
    @PostMapping("/crear")
    public String crearUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        if (!esAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para agregar usuarios.");
            return "redirect:/usuarios";
        }

        usuarioService.crearUsuario(usuario);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario agregado exitosamente.");
        return "redirect:/usuarios";
    }

    // 📌 Cargar formulario de edición de usuario (SOLO ADMINISTRADOR)
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        if (!esAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar usuarios.");
            return "redirect:/usuarios";
        }

        Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "editar-usuarios"; // Vista del formulario de edición
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/usuarios";
        }
    }

    // 📌 Actualizar usuario (SOLO ADMINISTRADOR)
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        if (!esAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar usuarios.");
            return "redirect:/usuarios";
        }

        Optional<Usuario> usuarioExistente = usuarioService.obtenerPorId(id);
        if (!usuarioExistente.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/usuarios";
        }

        Usuario usuarioDB = usuarioExistente.get();
        usuarioDB.setNombre(usuario.getNombre());
        usuarioDB.setEmail(usuario.getEmail());
        usuarioDB.setRol(usuario.getRol());

        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuarioDB.setPassword(passwordEncoder.encode(usuario.getPassword())); // Encriptamos la nueva contraseña
        }

        usuarioService.actualizarUsuario(usuarioDB);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado con éxito.");
        return "redirect:/usuarios";
    }

    // 📌 Eliminar usuario (SOLO ADMINISTRADOR)
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!esAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar usuarios.");
            return "redirect:/usuarios";
        }

        if (usuarioService.obtenerPorId(id).isPresent()) {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
        }
        return "redirect:/usuarios";
    }

    // 📌 Método privado para verificar si el usuario autenticado es ADMINISTRADOR
    private boolean esAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

}
