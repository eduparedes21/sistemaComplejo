package com.example.sistemaComplejoDeportivo.repository;

import com.example.sistemaComplejoDeportivo.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    List<Reserva> findByFechaReserva(LocalDate fecha);

    List<Reserva> findByUsuario_IdUsuario(Long idUsuario);
    
    List<Reserva> findByFechaReservaAndCancha_Id(LocalDate fecha, Long canchaId);


    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.cancha")
    List<Reserva> findAllWithCanchas();
    
    // Obtener reservas ordenadas por fecha de reserva (más recientes primero)
    List<Reserva> findAllByOrderByFechaReservaDesc();
}