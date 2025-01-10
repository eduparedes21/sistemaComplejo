package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Inventario;
import com.example.sistemaComplejoDeportivo.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Endpoint para listar todos los artículos
    @GetMapping
    public ResponseEntity<List<Inventario>> listarTodosLosArticulos() {
        List<Inventario> articulos = inventarioService.listarTodosLosArticulos();
        return ResponseEntity.ok(articulos);
    }

    // Endpoint para obtener un artículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerArticuloPorId(@PathVariable Integer id) {
        return inventarioService.obtenerArticuloPorId(id)
                .<ResponseEntity<?>>map(articulo -> ResponseEntity.ok(articulo))
                .orElseGet(() -> ResponseEntity.status(404).body("Artículo no encontrado con ID: " + id));
    }

    // Endpoint para crear un nuevo artículo
    @PostMapping
    public ResponseEntity<Inventario> crearArticulo(@RequestBody Inventario articulo) {
        Inventario nuevoArticulo = inventarioService.crearArticulo(articulo);
        return ResponseEntity.ok(nuevoArticulo);
    }

    // Endpoint para actualizar un artículo existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarArticulo(@PathVariable Integer id, @RequestBody Inventario articuloActualizado) {
        try {
            Inventario articulo = inventarioService.actualizarArticulo(id, articuloActualizado);
            return ResponseEntity.ok(articulo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Endpoint para eliminar un artículo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArticulo(@PathVariable Integer id) {
        try {
            inventarioService.eliminarArticulo(id);
            return ResponseEntity.ok("Artículo eliminado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
