package com.example.sistemaComplejoDeportivo.repository;

import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {

    List<MovimientoCaja> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    List<MovimientoCaja> findByTipoAndFechaHoraBetween(TipoMovimiento tipo, LocalDateTime inicio, LocalDateTime fin);

    List<MovimientoCaja> findByCategoriaAndFechaHoraBetween(String categoria, LocalDateTime inicio, LocalDateTime fin);
}
