package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.Inventario;
import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.model.TipoMovimiento;
import com.example.sistemaComplejoDeportivo.repository.InventarioRepository;
import com.example.sistemaComplejoDeportivo.repository.MovimientoCajaRepository;
import java.math.BigDecimal;
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

        // 📌 Si es un ingreso con producto, verificar y calcular el monto automáticamente
        if (movimientoCaja.getTipo() == TipoMovimiento.INGRESO && movimientoCaja.getInventario() != null) {
            Inventario producto = inventarioRepository.findById(movimientoCaja.getInventario().getIdArticulo())
                    .orElseThrow(() -> new Exception("Producto no encontrado en inventario"));

            if (producto.getCantidadStock() < movimientoCaja.getCantidad()) {
                throw new Exception("Stock insuficiente para la venta.");
            }

            // 📌 Calcular monto automáticamente según precio unitario
            if (producto.getPrecioUnitario() == null) {
                throw new Exception("El producto no tiene un precio unitario definido.");
            }

            // Calcular monto automáticamente convirtiendo a BigDecimal
            BigDecimal montoTotal = producto.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(movimientoCaja.getCantidad()));

            movimientoCaja.setMonto(montoTotal.doubleValue());

            // 📌 Asociar la categoría del producto automáticamente
            movimientoCaja.setInventario(producto); // ✅ Asignar la relación con Inventario

            // 📌 Restar la cantidad en stock
            producto.setCantidadStock(producto.getCantidadStock() - movimientoCaja.getCantidad());
            inventarioRepository.save(producto);
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
        return movimientoCajaRepository.findByInventario_CategoriaAndFechaHoraBetween(categoria, inicio, fin);
    }

    public List<MovimientoCaja> obtenerMovimientos() {
        List<MovimientoCaja> movimientos = movimientoCajaRepository.findAll();
        for (MovimientoCaja mov : movimientos) {
            if (mov.getInventario() != null) { // ✅ Asegurar que `Inventario` no sea nulo
                System.out.println("Producto: " + mov.getInventario().getNombre());
                System.out.println("Categoría: " + mov.getInventario().getCategoria()); // ✅ Corrección
            } else {
                System.out.println("❌ Error: El producto en este movimiento es nulo.");
            }
        }
        return movimientos;
    }

}
