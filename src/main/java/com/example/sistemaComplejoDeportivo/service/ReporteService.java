package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.repository.EgresoRepository;
import com.example.sistemaComplejoDeportivo.repository.IngresoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReporteService {

    @Autowired
    private IngresoRepository ingresoRepository;

    @Autowired
    private EgresoRepository egresoRepository;

    public Map<String, BigDecimal> generarReporte(LocalDateTime inicio, LocalDateTime fin) {
        BigDecimal totalIngresos = ingresoRepository.calcularTotalIngresos(inicio, fin);
        BigDecimal totalEgresos = egresoRepository.calcularTotalEgresos(inicio, fin);

        if (totalIngresos == null) {
            totalIngresos = BigDecimal.ZERO;
        }
        if (totalEgresos == null) {
            totalEgresos = BigDecimal.ZERO;
        }

        BigDecimal balance = totalIngresos.subtract(totalEgresos);

        Map<String, BigDecimal> reporte = new HashMap<>();
        reporte.put("totalIngresos", totalIngresos);
        reporte.put("totalEgresos", totalEgresos);
        reporte.put("balance", balance);

        return reporte;
    }
}
