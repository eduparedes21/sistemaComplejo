package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.Egreso;
import com.example.sistemaComplejoDeportivo.repository.EgresoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EgresoService {

    @Autowired
    private EgresoRepository egresoRepository;

    public Egreso crearEgreso(Egreso egreso) {
        return egresoRepository.save(egreso);
    }

    public List<Egreso> listarEgresos() {
        return egresoRepository.findAll();
    }

    public Optional<Egreso> obtenerEgresoPorId(Integer id) {
        return egresoRepository.findById(id);
    }

    public Egreso actualizarEgreso(Egreso egreso) {
        return egresoRepository.save(egreso);
    }

    public void eliminarEgreso(Integer id) {
        egresoRepository.deleteById(id);
    }
}
