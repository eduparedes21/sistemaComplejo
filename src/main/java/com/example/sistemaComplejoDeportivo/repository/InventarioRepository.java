package com.example.sistemaComplejoDeportivo.repository;

import com.example.sistemaComplejoDeportivo.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    // Buscar artículos por nombre (opcional, si necesitas una búsqueda específica)
    List<Inventario> findByNombreContainingIgnoreCase(String nombre);

    // Otros métodos personalizados pueden añadirse aquí si es necesario
}
