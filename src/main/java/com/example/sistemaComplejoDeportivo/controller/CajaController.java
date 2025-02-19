package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Inventario;
import com.example.sistemaComplejoDeportivo.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CajaController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/caja")
    public String mostrarCaja(Model model) {
        List<Inventario> productos = inventarioService.listarTodosLosArticulos();
        model.addAttribute("productos", productos); // 📌 Pasar los productos a la vista

        return "caja"; // 📌 Asegúrate de que el nombre coincida con tu archivo HTML
    }
}
