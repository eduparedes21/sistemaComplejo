package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.service.CanchaService;
import com.example.sistemaComplejoDeportivo.model.Canchas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/canchas")
public class CanchaController {

    @Autowired
    private CanchaService canchaService;

    @GetMapping
    public String listarCanchas(Model model) {
        model.addAttribute("canchas", canchaService.listarCanchas());
        return "canchas";
    }

    @PostMapping("/crear")
    public String crearCancha(@ModelAttribute Canchas cancha, RedirectAttributes redirectAttributes) {
        try {
            canchaService.guardarCancha(cancha);
            redirectAttributes.addFlashAttribute("mensaje", "Cancha creada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la cancha.");
        }
        return "redirect:/canchas"; // ✅ Redirige correctamente a la página de canchas
    }

    @GetMapping("/editar/{id}")
    public String editarCancha(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Canchas cancha = canchaService.obtenerCanchaPorId(id);
        if (cancha != null) {
            model.addAttribute("cancha", cancha);
            return "editar-cancha";
        }
        redirectAttributes.addFlashAttribute("error", "Canchas no encontrada.");
        return "redirect:/canchas";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCancha(@PathVariable Integer id, @ModelAttribute Canchas cancha, RedirectAttributes redirectAttributes) {
        canchaService.actualizarCancha(id, cancha);
        redirectAttributes.addFlashAttribute("mensaje", "Canchas actualizada correctamente.");
        return "redirect:/canchas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCancha(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        canchaService.eliminarCancha(id);
        redirectAttributes.addFlashAttribute("mensaje", "Canchas eliminada correctamente.");
        return "redirect:/canchas";
    }
}
