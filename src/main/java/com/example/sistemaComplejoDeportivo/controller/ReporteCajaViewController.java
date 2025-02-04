package com.example.sistemaComplejoDeportivo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/caja/reportes")
public class ReporteCajaViewController {

    @GetMapping
    public String mostrarPaginaReportes(Model model) {
        return "reportes"; // Retorna el archivo reportes.html desde /templates
    }
}
