package com.example.sistemaComplejoDeportivo.service;

import com.example.sistemaComplejoDeportivo.model.Ingreso;
import com.example.sistemaComplejoDeportivo.repository.IngresoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.sistemaComplejoDeportivo.exception.ResourceNotFoundException;


import java.util.List;
import java.util.Optional;

@Service
public class IngresoService {

    @Autowired
    private IngresoRepository ingresoRepository;

    public Ingreso crearIngreso(Ingreso ingreso) {
        return ingresoRepository.save(ingreso);
    }

    public List<Ingreso> listarIngresos() {
        return ingresoRepository.findAll();
    }

    public Optional<Ingreso> obtenerIngresoPorId(Integer id) {
        return ingresoRepository.findById(id);
    }

    public Ingreso actualizarIngreso(Ingreso ingreso) {
    Optional<Ingreso> ingresoExistente = ingresoRepository.findById(ingreso.getIdIngreso());

    if (!ingresoExistente.isPresent()) {
        throw new ResourceNotFoundException("Ingreso no encontrado con ID: " + ingreso.getIdIngreso());
    }

    Ingreso ingresoParaActualizar = ingresoExistente.get();

    // Actualizar sólo los campos que llegan en la solicitud
    ingresoParaActualizar.setMonto(ingreso.getMonto());
    ingresoParaActualizar.setConcepto(ingreso.getConcepto());

    // Si la fecha no está presente en la solicitud, mantenemos la fecha existente
    if (ingreso.getFecha() != null) {
        ingresoParaActualizar.setFecha(ingreso.getFecha());
    } else {
        ingresoParaActualizar.setFecha(ingresoParaActualizar.getFecha());
    }

    return ingresoRepository.save(ingresoParaActualizar);
}


    public boolean eliminarIngreso(Integer id) {
    System.out.println("Buscando ingreso con ID: " + id); // Para depuración
    Optional<Ingreso> ingresoOptional = ingresoRepository.findById(id);
    if (ingresoOptional.isPresent()) {
        ingresoRepository.deleteById(id);
        System.out.println("Ingreso eliminado con ID: " + id); // Para depuración
        return true;
    }
    System.out.println("Ingreso no encontrado con ID: " + id); // Para depuración
    return false;
}

}
