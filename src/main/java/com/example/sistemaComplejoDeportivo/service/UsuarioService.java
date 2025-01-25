package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    // 🛠️ **Crear usuario (por el administrador)**
    public Usuario crearUsuarioPersonal(String nombre, String email, String password) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password)); // 🔐 Se cifra la contraseña
        usuario.setRol("personal");
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getAuthenticatedUser() {
        // Obtén el contexto de autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("No hay un usuario autenticado");
        }
        // Obtén el email del usuario autenticado
        String email = authentication.getName();
        // Busca el usuario en la base de datos
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
