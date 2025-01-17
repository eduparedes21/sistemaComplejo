package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.exception.ResourceNotFoundException;
import com.example.sistemaComplejoDeportivo.model.Egreso;
import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.EgresoService;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/egresos")
public class EgresoController {

    @Autowired
    private EgresoService egresoService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> crearEgreso(@RequestBody Egreso egreso) {
        // Validar monto positivo
        if (egreso.getMonto() == null || egreso.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El monto debe ser un valor positivo.");
        }

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Buscar al usuario autenticado en la base de datos
        Optional<Usuario> usuarioOptional = usuarioService.obtenerPorEmail(email);
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autorizado.");
        }

        Usuario usuario = usuarioOptional.get();
        egreso.setUsuario(usuario); // Asociar el usuario al egreso

        // Asignar fecha actual si no se proporciona
        if (egreso.getFecha() == null) {
            egreso.setFecha(LocalDateTime.now());
        }

        egresoService.crearEgreso(egreso);
        return ResponseEntity.status(HttpStatus.CREATED).body("Egreso creado exitosamente.");
    }

    @GetMapping
    public ResponseEntity<List<Egreso>> listarEgresos() {
        return ResponseEntity.ok(egresoService.listarEgresos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEgresoPorId(@PathVariable Integer id) {
        Optional<Egreso> egreso = egresoService.obtenerEgresoPorId(id);
        return egreso.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEgreso(@PathVariable Integer id, @RequestBody Egreso egreso) {
        // Buscar egreso existente
        Optional<Egreso> egresoExistente = egresoService.obtenerEgresoPorId(id);
        if (!egresoExistente.isPresent()) {
            throw new ResourceNotFoundException("Egreso no encontrado con ID: " + id);
        }

        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Buscar al usuario autenticado en la base de datos
        Optional<Usuario> usuarioOptional = usuarioService.obtenerPorEmail(email);
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autorizado.");
        }

        Usuario usuario = usuarioOptional.get();

        // Actualizar campos necesarios
        Egreso egresoDB = egresoExistente.get();
        egresoDB.setMonto(egreso.getMonto() != null ? egreso.getMonto() : egresoDB.getMonto());
        egresoDB.setConcepto(egreso.getConcepto() != null ? egreso.getConcepto() : egresoDB.getConcepto());
        egresoDB.setUsuario(usuario); // Asignar el usuario que realizó la modificación
        egresoDB.setFecha(LocalDateTime.now()); // Fecha de modificación

        Egreso egresoActualizado = egresoService.actualizarEgreso(egresoDB);
        return ResponseEntity.ok(egresoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEgreso(@PathVariable Integer id) {
        Optional<Egreso> egresoExistente = egresoService.obtenerEgresoPorId(id);
        if (!egresoExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Egreso no encontrado.");
        }

        egresoService.eliminarEgreso(id);
        return ResponseEntity.ok("Egreso eliminado exitosamente.");
    }
}
