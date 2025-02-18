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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listarProveedores(Model model) {
        List<Proveedor> proveedores = proveedorService.obtenerTodosLosProveedores();
        model.addAttribute("proveedores", proveedores);
        return "proveedores"; // Asegúrate de crear esta vista
    }

    @GetMapping("/nuevo")
    public String formularioNuevoProveedor(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "crear-proveedor"; // Crear esta vista
    }

    @PostMapping("/crear")
    public String crearProveedor(@ModelAttribute Proveedor proveedor, RedirectAttributes redirectAttributes) {
        try {
            proveedorService.guardarProveedor(proveedor);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor agregado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar el proveedor: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditarProveedor(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Proveedor> proveedor = proveedorService.obtenerProveedorPorId(id);
        if (proveedor.isPresent()) {
            model.addAttribute("proveedor", proveedor.get());
            return "editar-proveedor"; // Asegúrate de crear esta vista
        } else {
            redirectAttributes.addFlashAttribute("error", "El proveedor no existe.");
            return "redirect:/proveedores";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarProveedor(@PathVariable Integer id, @ModelAttribute Proveedor proveedor, RedirectAttributes redirectAttributes) {
        try {
            proveedor.setIdProveedor(id); // Asegurar que el ID es el correcto
            proveedorService.guardarProveedor(proveedor);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor actualizado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el proveedor: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            proveedorService.eliminarProveedor(id);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/proveedores";
    }

}
