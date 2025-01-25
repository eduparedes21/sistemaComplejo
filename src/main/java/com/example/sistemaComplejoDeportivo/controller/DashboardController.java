package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    
    @Autowired
    private UsuarioService usuarioService; // Inyecta UsuarioService
    
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Llama al método getAuthenticatedUser desde UsuarioService
        Usuario usuario = usuarioService.getAuthenticatedUser();
        model.addAttribute("usuario", usuario);
        return "dashboard"; // Renderiza la vista dashboard.html
    }
}
