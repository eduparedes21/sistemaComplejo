package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Proveedor;
import com.example.sistemaComplejoDeportivo.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listarProveedores(Model model) {
        List<Proveedor> proveedores = proveedorService.obtenerTodosLosProveedores();
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("nuevoProveedor", new Proveedor());
        return "proveedores";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Integer id, Model model) {
        Optional<Proveedor> proveedorOptional = proveedorService.obtenerProveedorPorId(id);
        if (proveedorOptional.isPresent()) {
            model.addAttribute("proveedor", proveedorOptional.get());
            return "editarProveedor";
        } else {
            return "redirect:/proveedores";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarProveedor(@ModelAttribute Proveedor proveedor) {
        proveedorService.guardarProveedor(proveedor);
        return "redirect:/proveedores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Integer id) {
        proveedorService.eliminarProveedor(id);
        return "redirect:/proveedores";
    }
}
