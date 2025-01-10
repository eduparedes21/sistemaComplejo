package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.Inventario;
import com.example.sistemaComplejoDeportivo.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    // Listar todos los artículos
    public List<Inventario> listarTodosLosArticulos() {
        return inventarioRepository.findAll();
    }

    // Obtener un artículo por su ID
    public Optional<Inventario> obtenerArticuloPorId(Integer idArticulo) {
        return inventarioRepository.findById(idArticulo);
    }

    // Crear un nuevo artículo
    public Inventario crearArticulo(Inventario articulo) {
        return inventarioRepository.save(articulo);
    }

    // Actualizar un artículo existente
    public Inventario actualizarArticulo(Integer idArticulo, Inventario articuloActualizado) {
        Optional<Inventario> articuloExistente = inventarioRepository.findById(idArticulo);
        if (articuloExistente.isPresent()) {
            Inventario articulo = articuloExistente.get();
            articulo.setNombre(articuloActualizado.getNombre());
            articulo.setDescripcion(articuloActualizado.getDescripcion());
            articulo.setCantidadStock(articuloActualizado.getCantidadStock());
            articulo.setPrecioUnitario(articuloActualizado.getPrecioUnitario());
            articulo.setIdProveedor(articuloActualizado.getIdProveedor());
            return inventarioRepository.save(articulo);
        } else {
            throw new RuntimeException("El artículo con ID " + idArticulo + " no existe.");
        }
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
