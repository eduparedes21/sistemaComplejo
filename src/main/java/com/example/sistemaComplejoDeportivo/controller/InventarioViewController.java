package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventarioViewController {
    
    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("/inventario")
    public String mostrarInventario(Model model) {
        model.addAttribute("proveedores", proveedorService.obtenerTodosLosProveedores());
        return "inventario";
    }
}
