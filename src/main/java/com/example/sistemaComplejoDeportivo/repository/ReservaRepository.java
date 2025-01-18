package com.example.sistemaComplejoDeportivo.repository;

import com.example.sistemaComplejoDeportivo.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByFechaReserva(LocalDate fecha);
    List<Reserva> findByUsuario_IdUsuario(Long idUsuario);

}
