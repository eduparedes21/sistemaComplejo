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

        if (movimientoCaja.getMonto() < 0) {
            throw new Exception("El monto no puede ser negativo.");
        }

        if (movimientoCaja.getTipo() == TipoMovimiento.INGRESO && movimientoCaja.getInventario() != null) {
            Inventario producto = inventarioRepository.findById(movimientoCaja.getInventario().getIdArticulo())
                    .orElseThrow(() -> new Exception("Producto no encontrado en inventario"));

            if (producto.getCantidadStock() < movimientoCaja.getCantidad()) {
                throw new Exception("Stock insuficiente para la venta.");
            }

            if (producto.getPrecioUnitario() == null) {
                throw new Exception("El producto no tiene un precio unitario definido.");
            }

            BigDecimal montoTotal = producto.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(movimientoCaja.getCantidad()));

            movimientoCaja.setMonto(montoTotal.doubleValue());
            movimientoCaja.setInventario(producto);
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
        return movimientos.stream().mapToDouble(MovimientoCaja::getMonto).sum();
    }

    public List<MovimientoCaja> obtenerMovimientosPorTipoYFecha(TipoMovimiento tipo, LocalDateTime inicio, LocalDateTime fin) {
        return movimientoCajaRepository.findByTipoAndFechaHoraBetween(tipo, inicio, fin);
    }

    public List<MovimientoCaja> obtenerMovimientosPorCategoriaYFecha(String categoria, LocalDateTime inicio, LocalDateTime fin) {
        return movimientoCajaRepository.findByInventario_CategoriaAndFechaHoraBetween(categoria, inicio, fin);
    }

    public List<MovimientoCaja> obtenerMovimientos() {
        List<MovimientoCaja> movimientos = movimientoCajaRepository.findAll();
        for (MovimientoCaja mov : movimientos) {
            if (mov.getInventario() != null) {
                System.out.println("Producto: " + mov.getInventario().getNombre());
                System.out.println("Categoría: " + mov.getInventario().getCategoria());
            } else {
                System.out.println("❌ Error: El producto en este movimiento es nulo.");
            }
        }
        return movimientos;
    }
}
