package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import com.example.sistemaComplejoDeportivo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales incorrectas, intente nuevamente.");
        }
        return "login"; // Thymeleaf buscará login.html en /templates
    }

    @PostMapping("/admin/registrarPersonal")
    public ResponseEntity<?> registrarPersonal(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(password);
            usuario.setRol("personal"); // Asegura que tenga un rol válido

            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            return ResponseEntity.ok("Usuario creado: " + nuevoUsuario.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar usuario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public String processLogin() {
        return "redirect:/dashboard";
    }

}
