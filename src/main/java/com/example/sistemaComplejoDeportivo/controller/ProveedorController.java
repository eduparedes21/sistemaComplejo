package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Proveedor;
import com.example.sistemaComplejoDeportivo.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<Proveedor> obtenerTodos() {
        return proveedorService.obtenerTodosLosProveedores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        return proveedorService.obtenerProveedorPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Proveedor crearProveedor(@RequestBody Proveedor proveedor) {
        return proveedorService.guardarProveedor(proveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProveedor(@PathVariable Integer id, @RequestBody Proveedor proveedorActualizado) {
        return proveedorService.obtenerProveedorPorId(id)
                .map(proveedorExistente -> {
                    proveedorExistente.setNombreEmpresa(proveedorActualizado.getNombreEmpresa());
                    proveedorExistente.setContacto(proveedorActualizado.getContacto());
                    proveedorExistente.setTelefono(proveedorActualizado.getTelefono());
                    proveedorExistente.setDireccion(proveedorActualizado.getDireccion());
                    proveedorService.guardarProveedor(proveedorExistente);
                    return ResponseEntity.ok(proveedorExistente);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProveedor(@PathVariable Integer id) {
        if (proveedorService.obtenerProveedorPorId(id).isPresent()) {
            proveedorService.eliminarProveedor(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
