package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.repository.MovimientoCajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoCajaService {

    @Autowired
    private MovimientoCajaRepository movimientoCajaRepository;

    public MovimientoCaja registrarMovimiento(MovimientoCaja movimientoCaja) {
        // Configurar la fecha/hora automáticamente
        movimientoCaja.setFechaHora(LocalDateTime.now());

        // Guardar en la base de datos
        return movimientoCajaRepository.save(movimientoCaja);
    }

    public List<MovimientoCaja> obtenerMovimientosPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoCajaRepository.findByFechaHoraBetween(inicio, fin);
    }

    public double calcularBalance(LocalDateTime inicio, LocalDateTime fin) {
        List<MovimientoCaja> movimientos = obtenerMovimientosPorFecha(inicio, fin);
        return movimientos.stream().mapToDouble(mov -> mov.getMonto()).sum();
    }
}
