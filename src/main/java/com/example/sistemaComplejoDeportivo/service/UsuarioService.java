package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🔑 **Autenticación de usuario**
    public Optional<Usuario> autenticarUsuario(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(usuario -> passwordEncoder.matches(password, usuario.getPassword()));
    }

    // 🔐 **Método para verificar si el usuario autenticado es ADMIN**
    public boolean esAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }

        Optional<Usuario> usuario = usuarioRepository.findByEmail(authentication.getName());
        return usuario.isPresent() && "administrador".equals(usuario.get().getRol());
    }

    // 🛠️ **Método para crear un usuario con cualquier rol**
    public Usuario crearUsuario(Usuario usuario) {
        if (!esAdmin()) {
            throw new RuntimeException("No tienes permisos para agregar usuarios.");
        }

        // Verifica si el email ya está registrado
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        // Encripta la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    // 🛠️ **Actualizar un usuario**
    public Usuario actualizarUsuario(Usuario usuario) {
        if (!esAdmin()) {
            throw new RuntimeException("No tienes permisos para editar usuarios.");
        }

        return usuarioRepository.save(usuario);
    }

    // 🛠️ **Eliminar un usuario**
    public void eliminarUsuario(Long id) {
        if (!esAdmin()) {
            throw new RuntimeException("No tienes permisos para eliminar usuarios.");
        }

        usuarioRepository.deleteById(id);
    }

    // ✅ **Obtener usuario por ID**
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // ✅ **Obtener usuario por email**
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // ✅ **Listar todos los usuarios**
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // ✅ **Obtener usuario autenticado**
    public Usuario getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("No hay un usuario autenticado");
        }

        return usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
