package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.model.TipoMovimiento;
import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.MovimientoCajaService;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/caja")
public class MovimientoCajaController {

    @Autowired
    private MovimientoCajaService movimientoCajaService;
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMovimiento(
            @RequestParam String categoria,
            @RequestParam(required = false) String descripcion,
            @RequestParam Double monto,
            @RequestParam String tipo,
            Principal principal) {

        // Obtener usuario autenticado
        String emailUsuario = principal.getName(); // Asegúrate de que el usuario esté autenticado
        Usuario usuario = usuarioService.obtenerPorEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear y guardar el movimiento
        MovimientoCaja movimiento = new MovimientoCaja();
        movimiento.setCategoria(categoria);
        movimiento.setDescripcion(descripcion);
        movimiento.setMonto(monto);
        movimiento.setTipo(TipoMovimiento.valueOf(tipo));
        movimiento.setUsuario(usuario);

        MovimientoCaja movimientoGuardado = movimientoCajaService.registrarMovimiento(movimiento);

        return ResponseEntity.ok("Movimiento registrado con éxito.");
    }

    @GetMapping("/movimientos")
    public List<MovimientoCaja> obtenerMovimientos(@RequestParam String inicio, @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return movimientoCajaService.obtenerMovimientosPorFecha(fechaInicio, fechaFin);
    }

    @GetMapping("/balance")
    public double calcularBalance(@RequestParam String inicio, @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return movimientoCajaService.calcularBalance(fechaInicio, fechaFin);
    }
}
