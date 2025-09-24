package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.dto.MovimientoDTO;
import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.model.TipoMovimiento;
import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.MovimientoCajaService;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

import java.security.Principal;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/caja")
public class MovimientoCajaController {

    @Autowired
    private MovimientoCajaService movimientoCajaService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMovimiento(@RequestBody MovimientoDTO dto, Principal principal) {
        try {
            Usuario usuario = usuarioService.obtenerPorEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            MovimientoCaja movimiento = movimientoCajaService.crearMovimientoDesdeDTO(dto, usuario);
            return ResponseEntity.ok("Movimiento registrado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/reportes/descargar")
    public void descargarReporteCSV(@RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
                                     @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
                                     HttpServletResponse response) {

        // Configurar el tipo de contenido y el nombre del archivo para la respuesta HTTP
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"reporte_movimientos.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            // Escribir la primera línea del CSV (encabezados)
            writer.println("ID,Fecha y Hora,Producto,Categoria,Tipo,Descripcion,Cantidad,Monto,Usuario");

            // Obtener los datos del servicio. La consulta del repositorio es más eficiente.
            List<MovimientoCaja> movimientos = movimientoCajaService.obtenerMovimientosPorRangoDeFechas(inicio, fin);

            // Escribir cada movimiento en el CSV
            for (MovimientoCaja mov : movimientos) {
                String usuarioNombre = (mov.getUsuario() != null) ? mov.getUsuario().getNombre() : "N/A";
                String inventarioNombre = (mov.getInventario() != null) ? mov.getInventario().getNombre() : "N/A";
                String categoria = (mov.getInventario() != null && mov.getInventario().getCategoria() != null) ? mov.getInventario().getCategoria() : "Sin categoría";

                // Usar un StringBuilder para construir la línea del CSV
                StringBuilder sb = new StringBuilder();
                sb.append(mov.getId()).append(",");
                sb.append(mov.getFechaHora()).append(",");
                sb.append("\"").append(inventarioNombre).append("\","); // Usar comillas para nombres con comas
                sb.append("\"").append(categoria).append("\",");
                sb.append(mov.getTipo()).append(",");
                sb.append("\"").append(mov.getDescripcion()).append("\",");
                sb.append(mov.getCantidad() != null ? mov.getCantidad() : 0).append(",");
                sb.append(String.format("%.2f", mov.getMonto())).append(",");
                sb.append("\"").append(usuarioNombre).append("\"");
                writer.println(sb.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
