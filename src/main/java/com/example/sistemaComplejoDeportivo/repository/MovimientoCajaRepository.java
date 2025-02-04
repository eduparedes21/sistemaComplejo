package com.example.sistemaComplejoDeportivo.repository;

import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {

    List<MovimientoCaja> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    List<MovimientoCaja> findByTipoAndFechaHoraBetween(TipoMovimiento tipo, LocalDateTime inicio, LocalDateTime fin);

    List<MovimientoCaja> findByCategoriaAndFechaHoraBetween(String categoria, LocalDateTime inicio, LocalDateTime fin);
}
