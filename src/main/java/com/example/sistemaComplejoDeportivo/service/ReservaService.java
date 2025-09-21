package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.exception.ResourceNotFoundException;
import com.example.sistemaComplejoDeportivo.model.Reserva;
import com.example.sistemaComplejoDeportivo.repository.ReservaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva crearReserva(Reserva reserva) {
        if (hayConflictoDeHorario(reserva.getFechaReserva(), reserva.getHoraInicio(), reserva.getHoraFin(),reserva.getCancha().getId().longValue())) {
            throw new RuntimeException("Ya existe una reserva en ese horario.");
        }
        return reservaRepository.save(reserva);
    }

    public boolean hayConflictoDeHorario(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Long canchaId) {
        List<Reserva> reservasEnFechaYCancha = reservaRepository.findByFechaReservaAndCancha_Id(fecha, canchaId);
        for (Reserva r : reservasEnFechaYCancha) {
            if (!(horaFin.isBefore(r.getHoraInicio()) || horaInicio.isAfter(r.getHoraFin()))) {
                return true; // Existe conflicto en esta cancha
            }
        }
        return false;
    }

    public List<Reserva> listarReservas() {
        return reservaRepository.findAllWithCanchas();
    }

    public List<Reserva> listarReservasOrdenadas() {
        return reservaRepository.findAllByOrderByFechaReservaDesc(); // Ordenar por fecha en orden descendente
    }

    public Optional<Reserva> obtenerReservaPorId(Integer id) {
        return reservaRepository.findById(id);
    }

    public Reserva actualizarReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public boolean eliminarReserva(Integer id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        if (!reserva.isPresent()) {
            throw new ResourceNotFoundException("Reserva no encontrada con ID: " + id);
        }
        reservaRepository.deleteById(id);
        return true;
    }
}
