package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.exception.ResourceNotFoundException;
import com.example.sistemaComplejoDeportivo.model.Reserva;
import com.example.sistemaComplejoDeportivo.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva crearReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
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
