package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.model.TipoMovimiento;
import com.example.sistemaComplejoDeportivo.service.MovimientoCajaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/caja/reportes")
public class ReporteCajaController {

    @Autowired
    private MovimientoCajaService movimientoCajaService;

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

    @GetMapping("/movimientos/tipo")
    public List<MovimientoCaja> obtenerMovimientosPorTipo(
            @RequestParam String inicio,
            @RequestParam String fin,
            @RequestParam String tipo) {

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);
        TipoMovimiento tipoMovimiento = TipoMovimiento.valueOf(tipo);

        return movimientoCajaService.obtenerMovimientosPorTipoYFecha(tipoMovimiento, fechaInicio, fechaFin);
    }

    @GetMapping("/movimientos/categoria")
    public List<MovimientoCaja> obtenerMovimientosPorCategoria(
            @RequestParam String inicio,
            @RequestParam String fin,
            @RequestParam String categoria) {

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);

        return movimientoCajaService.obtenerMovimientosPorCategoriaYFecha(categoria, fechaInicio, fechaFin);
    }
}
