package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Autenticación de usuario
    public Optional<Usuario> autenticarUsuario(String email, String contraseña) {
        return usuarioRepository.findByEmail(email)
                .filter(usuario -> passwordEncoder.matches(contraseña, usuario.getContraseña()));
    }

    // Crear usuario (por el administrador)
    public Usuario crearUsuarioPersonal(String nombre, String email, String contraseña) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setContraseña(passwordEncoder.encode(contraseña));
        usuario.setRol("personal");
        return usuarioRepository.save(usuario);
    }
}
