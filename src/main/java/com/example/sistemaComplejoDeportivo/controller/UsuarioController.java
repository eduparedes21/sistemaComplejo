package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarios"; // Renderiza templates/usuarios.html
    }
}
