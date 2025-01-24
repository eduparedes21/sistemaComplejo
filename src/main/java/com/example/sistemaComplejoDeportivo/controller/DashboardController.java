package com.example.sistemaComplejoDeportivo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard"; // Thymeleaf buscará dashboard.html en /templates
    }
}
