package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Inventario;
import com.example.sistemaComplejoDeportivo.repository.InventarioRepository;
import com.example.sistemaComplejoDeportivo.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;
    @Autowired
    private InventarioRepository inventarioRepository;

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
        Optional<Inventario> articuloExistente = inventarioRepository.findById(id);

        if (!articuloExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: No se encontró el producto con ID: " + id);
        }

        Inventario articulo = articuloExistente.get();

        // Asignar valores si existen
        articulo.setNombre(articuloActualizado.getNombre());
        articulo.setCategoria(articuloActualizado.getCategoria());
        articulo.setDescripcion(articuloActualizado.getDescripcion());
        articulo.setPrecioUnitario(articuloActualizado.getPrecioUnitario());

        // Asegurar que cantidadStock no sea null
        if (articuloActualizado.getCantidadStock() == null) {
            articulo.setCantidadStock(0);
        } else {
            articulo.setCantidadStock(articuloActualizado.getCantidadStock());
        }

        articulo.setProveedor(articuloActualizado.getProveedor());

        inventarioRepository.save(articulo);

        return ResponseEntity.ok("Producto actualizado correctamente.");
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
