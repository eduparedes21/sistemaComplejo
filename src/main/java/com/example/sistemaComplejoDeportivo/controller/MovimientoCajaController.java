package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Inventario;
import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.model.TipoMovimiento;
import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.MovimientoCajaService;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import com.example.sistemaComplejoDeportivo.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/caja")
public class MovimientoCajaController {

    @Autowired
    private MovimientoCajaService movimientoCajaService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private InventarioService inventarioService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMovimiento(
            @RequestParam(required = false) String descripcion,
            @RequestParam String tipo,
            @RequestParam(required = false) Integer idArticulo,
            @RequestParam(required = false) Integer cantidad,
            Principal principal) {

        try {
            String emailUsuario = principal.getName();
            Usuario usuario = usuarioService.obtenerPorEmail(emailUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            MovimientoCaja movimiento = new MovimientoCaja();
            movimiento.setDescripcion(descripcion);
            movimiento.setTipo(TipoMovimiento.valueOf(tipo));
            movimiento.setUsuario(usuario);
            movimiento.setFechaHora(LocalDateTime.now());

            if (idArticulo != null && cantidad != null) {
                Inventario producto = inventarioService.obtenerArticuloPorId(idArticulo)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                movimiento.setInventario(producto);
                movimiento.setCantidad(cantidad);
            }

            MovimientoCaja movimientoGuardado = movimientoCajaService.registrarMovimiento(movimiento);

            return ResponseEntity.ok("Movimiento registrado con éxito.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
