package com.example.sistemaComplejoDeportivo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CajaController {

    @GetMapping("/caja")
    public String mostrarModuloCaja(Model model) {
        // Aquí puedes cargar datos iniciales si es necesario
        return "caja"; // Nombre del archivo HTML en /resources/templates/
    }
}
