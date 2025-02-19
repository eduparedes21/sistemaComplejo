package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.Inventario;
import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.model.TipoMovimiento;
import com.example.sistemaComplejoDeportivo.repository.InventarioRepository;
import com.example.sistemaComplejoDeportivo.repository.MovimientoCajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoCajaService {

    @Autowired
    private MovimientoCajaRepository movimientoCajaRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Transactional
    public MovimientoCaja registrarMovimiento(MovimientoCaja movimientoCaja) throws Exception {
        movimientoCaja.setFechaHora(LocalDateTime.now());

        // 📌 Si es un ingreso con producto, verificar y descontar stock
        if (movimientoCaja.getTipo() == TipoMovimiento.INGRESO && movimientoCaja.getProducto() != null) {
            Inventario producto = inventarioRepository.findById(movimientoCaja.getProducto().getIdArticulo())
                    .orElseThrow(() -> new Exception("Producto no encontrado en inventario"));

            if (producto.getCantidadStock() < movimientoCaja.getCantidad()) {
                throw new Exception("Stock insuficiente para la venta.");
            }

            // 📌 Restar la cantidad en stock y guardar el cambio
            producto.setCantidadStock(producto.getCantidadStock() - movimientoCaja.getCantidad());
            inventarioRepository.save(producto); // Guardar la actualización del inventario
        }

        return movimientoCajaRepository.save(movimientoCaja);
    }

    public List<MovimientoCaja> obtenerMovimientosPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoCajaRepository.findByFechaHoraBetween(inicio, fin);
    }

    public double calcularBalance(LocalDateTime inicio, LocalDateTime fin) {
        List<MovimientoCaja> movimientos = obtenerMovimientosPorFecha(inicio, fin);
        return movimientos.stream().mapToDouble(mov -> mov.getMonto()).sum();
    }

    // 📌 Se restauran los métodos eliminados para ReporteCajaController
    public List<MovimientoCaja> obtenerMovimientosPorTipoYFecha(TipoMovimiento tipo, LocalDateTime inicio, LocalDateTime fin) {
        return movimientoCajaRepository.findByTipoAndFechaHoraBetween(tipo, inicio, fin);
    }

    public List<MovimientoCaja> obtenerMovimientosPorCategoriaYFecha(String categoria, LocalDateTime inicio, LocalDateTime fin) {
        return movimientoCajaRepository.findByCategoriaAndFechaHoraBetween(categoria, inicio, fin);
    }
}
