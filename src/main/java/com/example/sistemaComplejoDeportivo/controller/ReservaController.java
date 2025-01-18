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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Usuario> usuarioOptional = usuarioService.obtenerPorEmail(email);
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autorizado");
        }

        Usuario usuario = usuarioOptional.get();
        reserva.setUsuario(usuario); // Asociar usuario
        if (reserva.getFechaReserva() == null || reserva.getHoraInicio() == null || reserva.getHoraFin() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fecha y horas son obligatorias");
        }

        Reserva nuevaReserva = reservaService.crearReserva(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        return ResponseEntity.ok(reservaService.listarReservas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerReservaPorId(@PathVariable Integer id) {
        Optional<Reserva> reserva = reservaService.obtenerReservaPorId(id);
        return reserva.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        // Buscar reserva existente
        Optional<Reserva> reservaExistente = reservaService.obtenerReservaPorId(id);
        if (!reservaExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada");
        }

        Reserva reservaDB = reservaExistente.get();

        // Actualizar los campos según los datos enviados
        reservaDB.setFechaReserva(reserva.getFechaReserva() != null ? reserva.getFechaReserva() : reservaDB.getFechaReserva());
        reservaDB.setHoraInicio(reserva.getHoraInicio() != null ? reserva.getHoraInicio() : reservaDB.getHoraInicio());
        reservaDB.setHoraFin(reserva.getHoraFin() != null ? reserva.getHoraFin() : reservaDB.getHoraFin());
        reservaDB.setDescripcion(reserva.getDescripcion() != null ? reserva.getDescripcion() : reservaDB.getDescripcion());
        reservaDB.setMontoTotal(reserva.getMontoTotal() != null ? reserva.getMontoTotal() : reservaDB.getMontoTotal());

        // Guardar los cambios
        Reserva reservaActualizada = reservaService.actualizarReserva(reservaDB);
        return ResponseEntity.ok(reservaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Integer id) {
        try {
            reservaService.eliminarReserva(id);
            return ResponseEntity.ok("Reserva eliminada exitosamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
