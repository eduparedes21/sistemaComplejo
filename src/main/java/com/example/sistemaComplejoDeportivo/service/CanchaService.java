package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.Canchas;
import com.example.sistemaComplejoDeportivo.repository.CanchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanchaService {

    @Autowired
    private CanchaRepository canchaRepository;

    public List<Canchas> listarCanchas() {
        return canchaRepository.findAll();
    }

    public Canchas obtenerCanchaPorId(Integer id) {
        return canchaRepository.findById(id).orElse(null);
    }

    public void guardarCancha(Canchas cancha) {
        canchaRepository.save(cancha);
    }

    public void actualizarCancha(Integer id, Canchas canchaActualizada) {
        Canchas cancha = obtenerCanchaPorId(id);
        if (cancha != null) {
            cancha.setNombre(canchaActualizada.getNombre());
            cancha.setTipo(canchaActualizada.getTipo());
            cancha.setEstado(canchaActualizada.getEstado());
            cancha.setCapacidad(canchaActualizada.getCapacidad());
            cancha.setPrecioHora(canchaActualizada.getPrecioHora());
            canchaRepository.save(cancha);
        }
    }

    public void eliminarCancha(Integer id) {
        canchaRepository.deleteById(id);
    }
}
