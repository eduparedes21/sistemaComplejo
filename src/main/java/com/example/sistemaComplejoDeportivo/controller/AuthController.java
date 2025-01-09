package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        Optional<Usuario> usuario = usuarioService.autenticarUsuario(email, password);
        if (usuario.isPresent()) {
            return ResponseEntity.ok("Inicio de sesión exitoso, rol: " + usuario.get().getRol());
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }

    @PostMapping("/admin/registrarPersonal")
    public ResponseEntity<?> registrarPersonal(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password) {
        Usuario nuevoUsuario = usuarioService.crearUsuarioPersonal(nombre, email, password);
        return ResponseEntity.ok("Usuario personal creado: " + nuevoUsuario.getEmail());
    }
}