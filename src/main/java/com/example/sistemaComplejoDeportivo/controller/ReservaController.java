package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.exception.ResourceNotFoundException;
import com.example.sistemaComplejoDeportivo.model.Reserva;
import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.ReservaService;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public String crearReserva(@ModelAttribute Reserva reserva, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Usuario> usuarioOptional = usuarioService.obtenerPorEmail(email);
        if (!usuarioOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no autorizado");
            return "redirect:/reservas";
        }

        Usuario usuario = usuarioOptional.get();
        reserva.setUsuario(usuario); // Asociar usuario

        // Validación de datos obligatorios
        if (reserva.getFechaReserva() == null || reserva.getHoraInicio() == null || reserva.getHoraFin() == null) {
            redirectAttributes.addFlashAttribute("error", "Fecha y horas son obligatorias");
            return "redirect:/reservas";
        }

        try {
            reservaService.crearReserva(reserva);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva registrada con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al guardar la reserva.Ya existe una reserva en el mismo horario.");
        }

        return "redirect:/reservas";
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        return ResponseEntity.ok(reservaService.listarReservas());
    }

    // 📌 Cargar formulario de edición con datos de la reserva
    @GetMapping("/editar/{id}")
    public String editarReserva(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Reserva> reserva = reservaService.obtenerReservaPorId(id);
        if (reserva.isPresent()) {
            model.addAttribute("reserva", reserva.get());
            return "editar-reserva"; // HTML de edición
        } else {
            redirectAttributes.addFlashAttribute("error", "La reserva no existe.");
            return "redirect:/reservas";
        }
    }

    // 📌 Actualizar una reserva existente
    @PostMapping("/actualizar/{id}")
    public String actualizarReserva(@PathVariable Integer id, @ModelAttribute Reserva reserva, RedirectAttributes redirectAttributes) {
        Optional<Reserva> reservaExistente = reservaService.obtenerReservaPorId(id);
        if (!reservaExistente.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "No se encontró la reserva.");
            return "redirect:/reservas";
        }

        Reserva reservaDB = reservaExistente.get();
        reservaDB.setFechaReserva(reserva.getFechaReserva());
        reservaDB.setHoraInicio(reserva.getHoraInicio());
        reservaDB.setHoraFin(reserva.getHoraFin());
        reservaDB.setDescripcion(reserva.getDescripcion());
        reservaDB.setMontoTotal(reserva.getMontoTotal());

        reservaService.actualizarReserva(reservaDB);
        redirectAttributes.addFlashAttribute("mensaje", "Reserva actualizada con éxito.");

        return "redirect:/reservas"; // Redirige a la lista de reservas
    }

    // 📌 Eliminar reserva
    @GetMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        if (reservaService.obtenerReservaPorId(id).isPresent()) {
            reservaService.eliminarReserva(id);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva eliminada correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró la reserva.");
        }
        return "redirect:/reservas";
    }

}
