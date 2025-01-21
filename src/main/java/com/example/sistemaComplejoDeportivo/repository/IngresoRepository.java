package com.example.sistemaComplejoDeportivo.repository;

import com.example.sistemaComplejoDeportivo.model.Ingreso;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IngresoRepository extends JpaRepository<Ingreso, Integer> {

    @Query("SELECT SUM(i.monto) FROM Ingreso i WHERE i.fecha BETWEEN :inicio AND :fin")
    BigDecimal calcularTotalIngresos(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    List<Ingreso> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
