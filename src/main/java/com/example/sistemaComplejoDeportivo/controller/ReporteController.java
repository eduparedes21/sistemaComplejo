package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/financieros")
    public ResponseEntity<?> obtenerReporte(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio, formatter);
            LocalDateTime fin = LocalDateTime.parse(fechaFin, formatter);

            // Validación: inicio debe ser antes que fin
            if (inicio.isAfter(fin)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "La fecha de inicio no puede ser posterior a la fecha de fin.");
                return ResponseEntity.badRequest().body(error);
            }

            Map<String, BigDecimal> reporte = reporteService.generarReporte(inicio, fin);

            return ResponseEntity.ok(reporte);
        } catch (DateTimeParseException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Formato de fecha inválido. Use el formato: yyyy-MM-dd'T'HH:mm:ss");
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ocurrió un error inesperado: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
