package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.dto.MovimientoDTO;
import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.model.TipoMovimiento;
import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.MovimientoCajaService;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/caja")
public class MovimientoCajaController {

    @Autowired
    private MovimientoCajaService movimientoCajaService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMovimiento(@RequestBody MovimientoDTO dto, Principal principal) {
        try {
            Usuario usuario = usuarioService.obtenerPorEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            MovimientoCaja movimiento = movimientoCajaService.crearMovimientoDesdeDTO(dto, usuario);
            return ResponseEntity.ok("Movimiento registrado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
