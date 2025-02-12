package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.Inventario;
import com.example.sistemaComplejoDeportivo.repository.InventarioRepository;
import com.example.sistemaComplejoDeportivo.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;

    // Listar todos los artículos
    public List<Inventario> listarTodosLosArticulos() {
        return inventarioRepository.findAll();
    }

    // Obtener un artículo por su ID
    public Optional<Inventario> obtenerArticuloPorId(Integer idArticulo) {
        return inventarioRepository.findById(idArticulo);
    }

    public Inventario crearArticulo(Inventario articulo) {
        if (articulo.getProveedor() != null && articulo.getProveedor().getIdProveedor() != null) {
            articulo.setProveedor(proveedorRepository.findById(articulo.getProveedor().getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado")));
        }
        return inventarioRepository.save(articulo);
    }

    public Inventario actualizarArticulo(Integer idArticulo, Inventario articuloActualizado) {
        Inventario articuloExistente = inventarioRepository.findById(idArticulo)
                .orElseThrow(() -> new RuntimeException("El artículo con ID " + idArticulo + " no existe."));
        articuloExistente.setNombre(articuloActualizado.getNombre());
        articuloExistente.setDescripcion(articuloActualizado.getDescripcion());
        articuloExistente.setCantidadStock(articuloActualizado.getCantidadStock());
        articuloExistente.setPrecioUnitario(articuloActualizado.getPrecioUnitario());
        if (articuloActualizado.getProveedor() != null && articuloActualizado.getProveedor().getIdProveedor() != null) {
            articuloExistente.setProveedor(proveedorRepository.findById(articuloActualizado.getProveedor().getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado")));
        }
        return inventarioRepository.save(articuloExistente);
    }

    // Eliminar un artículo por su ID
    public void eliminarArticulo(Integer idArticulo) {
        if (inventarioRepository.existsById(idArticulo)) {
            inventarioRepository.deleteById(idArticulo);
        } else {
            throw new RuntimeException("El artículo con ID " + idArticulo + " no existe.");
        }
    }
}
