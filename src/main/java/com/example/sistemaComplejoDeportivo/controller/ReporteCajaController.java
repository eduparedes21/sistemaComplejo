/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.model.TipoMovimiento;
import com.example.sistemaComplejoDeportivo.service.MovimientoCajaService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/caja/reportes")
public class ReporteCajaController {

    @Autowired
    private MovimientoCajaService movimientoCajaService;

    @GetMapping("/movimientos")
    public List<MovimientoCaja> obtenerMovimientos(@RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fin) {
        if (inicio == null || fin == null || inicio.isEmpty() || fin.isEmpty()) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden estar vacías.");
        }

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);

        return movimientoCajaService.obtenerMovimientosPorFecha(fechaInicio, fechaFin);
    }

    @GetMapping("/movimientos/tipo")
public ResponseEntity<?> obtenerMovimientosPorTipo(
        @RequestParam String tipo,
        @RequestParam String inicio,
        @RequestParam String fin) {
    try {
        // Convertir el tipo al enum TipoMovimiento
        TipoMovimiento tipoMovimiento = TipoMovimiento.valueOf(tipo.toUpperCase());

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);

        // Llamar al servicio con el enum
        List<MovimientoCaja> movimientos = movimientoCajaService.obtenerMovimientosPorTipoYFecha(tipoMovimiento, fechaInicio, fechaFin);
        return ResponseEntity.ok(movimientos);

    } catch (IllegalArgumentException e) {
        // Manejar si el tipo no es válido
        return ResponseEntity.badRequest().body("Tipo inválido. Debe ser 'INGRESO' o 'EGRESO'.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al procesar el filtro.");
    }
}


    @GetMapping("/movimientos/categoria")
    public List<MovimientoCaja> obtenerMovimientosPorCategoria(
            @RequestParam String categoria,
            @RequestParam String inicio,
            @RequestParam String fin) {

        // Decodificar la categoría por si llega con caracteres especiales
        String categoriaDecodificada = URLDecoder.decode(categoria, StandardCharsets.UTF_8);

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);

        return movimientoCajaService.obtenerMovimientosPorCategoriaYFecha(categoriaDecodificada, fechaInicio, fechaFin);
    }

    @GetMapping("/balance")
    public double calcularBalance(@RequestParam String inicio, @RequestParam String fin) {
        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        return movimientoCajaService.calcularBalance(fechaInicio, fechaFin);
    }
}
