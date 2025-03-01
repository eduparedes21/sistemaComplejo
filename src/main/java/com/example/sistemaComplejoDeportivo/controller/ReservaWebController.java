package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Reserva;
import com.example.sistemaComplejoDeportivo.model.Canchas;
import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.CanchaService;
import com.example.sistemaComplejoDeportivo.service.ReservaService;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservas")
public class ReservaWebController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private CanchaService canchaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarReservas(Model model) {
        List<Reserva> reservas = reservaService.listarReservas();
        List<Canchas> canchas = canchaService.listarCanchas(); // Añadir las canchas al modelo
        model.addAttribute("reservas", reservas);
        model.addAttribute("canchas", canchas);
        return "reservas";
    }

    @GetMapping("/crear")
    public String mostrarFormularioReserva(Model model) {
        List<Canchas> canchas = canchaService.listarCanchas(); // Obtener todas las canchas
        System.out.println("Cancha lista: " + canchas);  // Verifica que canchas no esté vacío
        model.addAttribute("canchas", canchas); // Agregar las canchas al modelo
        model.addAttribute("reserva", new Reserva()); // Aseguramos que el objeto de reserva esté vacío
        return "reservas"; // Retornar la vista
    }

    @PostMapping("/crear")
    public String crearReserva(@ModelAttribute Reserva reserva, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<Usuario> usuarioOptional = usuarioService.obtenerPorEmail(email);
            if (!usuarioOptional.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Usuario no autorizado.");
                return "redirect:/reservas";
            }
            Usuario usuario = usuarioOptional.get();
            reserva.setUsuario(usuario);

            if (reserva.getFechaReserva() == null || reserva.getHoraInicio() == null || reserva.getHoraFin() == null) {
                redirectAttributes.addFlashAttribute("error", "Fecha y horas son obligatorias.");
                return "redirect:/reservas";
            }

            reservaService.crearReserva(reserva);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva registrada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al registrar la reserva: " + e.getMessage());
        }
        return "redirect:/reservas";
    }

    @GetMapping("/editar/{id}")
    public String editarReserva(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Reserva> reserva = reservaService.obtenerReservaPorId(id);
        List<Canchas> canchas = canchaService.listarCanchas();
        if (reserva.isPresent()) {
            model.addAttribute("reserva", reserva.get());
            model.addAttribute("canchas", canchas);
            return "editar-reserva";
        } else {
            redirectAttributes.addFlashAttribute("error", "La reserva no existe.");
            return "redirect:/reservas";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarReserva(@PathVariable Integer id, @ModelAttribute Reserva reserva, RedirectAttributes redirectAttributes) {
        try {
            reserva.setIdReserva(id);
            reservaService.actualizarReserva(reserva);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva actualizada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la reserva: " + e.getMessage());
        }
        return "redirect:/reservas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            reservaService.eliminarReserva(id);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la reserva: " + e.getMessage());
        }
        return "redirect:/reservas";
    }
}
