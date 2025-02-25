package com.example.sistemaComplejoDeportivo.repository;

import com.example.sistemaComplejoDeportivo.model.MovimientoCaja;
import com.example.sistemaComplejoDeportivo.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {

    List<MovimientoCaja> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    List<MovimientoCaja> findByTipoAndFechaHoraBetween(TipoMovimiento tipo, LocalDateTime inicio, LocalDateTime fin);

    List<MovimientoCaja> findByInventario_CategoriaAndFechaHoraBetween(String categoria, LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT mc FROM MovimientoCaja mc JOIN FETCH mc.inventario i LEFT JOIN FETCH mc.usuario u WHERE mc.fechaHora BETWEEN :inicio AND :fin")
    List<MovimientoCaja> obtenerMovimientosConInventario(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

}
