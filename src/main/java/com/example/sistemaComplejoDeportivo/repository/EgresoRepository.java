package com.example.sistemaComplejoDeportivo.repository;

import com.example.sistemaComplejoDeportivo.model.Egreso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EgresoRepository extends JpaRepository<Egreso, Integer> {
    @Query("SELECT SUM(e.monto) FROM Egreso e WHERE e.fecha BETWEEN :inicio AND :fin")
    BigDecimal calcularTotalEgresos(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    List<Egreso> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
