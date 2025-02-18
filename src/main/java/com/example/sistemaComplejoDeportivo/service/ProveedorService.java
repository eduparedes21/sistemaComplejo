package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.Proveedor;
import com.example.sistemaComplejoDeportivo.repository.InventarioRepository;
import com.example.sistemaComplejoDeportivo.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> obtenerProveedorPorId(Integer id) {
        return proveedorRepository.findById(id);
    }

    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public void eliminarProveedor(Integer id) throws Exception {
        if (inventarioRepository.existsByProveedor_IdProveedor(id)) {
            throw new Exception("No se puede eliminar el proveedor porque tiene productos en el inventario.");
        }
        proveedorRepository.deleteById(id);
    }

}
