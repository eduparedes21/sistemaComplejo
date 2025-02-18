package com.example.sistemaComplejoDeportivo.repository;

import com.example.sistemaComplejoDeportivo.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
    // 📌 Buscar productos por nombre
    List<Inventario> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByProveedor_IdProveedor(Integer idProveedor);
}

