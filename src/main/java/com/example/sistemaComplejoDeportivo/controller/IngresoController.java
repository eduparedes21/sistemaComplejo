package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.exception.ResourceNotFoundException;
import com.example.sistemaComplejoDeportivo.model.Ingreso;
import com.example.sistemaComplejoDeportivo.model.Usuario;
import com.example.sistemaComplejoDeportivo.service.IngresoService;
import com.example.sistemaComplejoDeportivo.service.UsuarioService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/ingresos")
public class IngresoController {

    @Autowired
    private IngresoService ingresoService;
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> crearIngreso(@RequestBody Ingreso ingreso) {

        if (ingreso.getMonto() == null || ingreso.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El monto debe ser un valor positivo.");
        }
        // Obtén el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Email del usuario autenticado

        // Busca el usuario en la base de datos
        Optional<Usuario> usuarioOptional = usuarioService.obtenerPorEmail(email);
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autorizado");
        }

        Usuario usuario = usuarioOptional.get();
        ingreso.setUsuario(usuario); // Asocia el usuario al ingreso

        // Si la fecha no se especifica, asigna la fecha actual
        if (ingreso.getFecha() == null) {
            ingreso.setFecha(LocalDateTime.now());
        }

        ingresoService.crearIngreso(ingreso);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ingreso creado exitosamente");
    }

    @GetMapping
    public ResponseEntity<List<Ingreso>> listarIngresos() {
        return ResponseEntity.ok(ingresoService.listarIngresos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerIngresoPorId(@PathVariable Integer id) {
        Optional<Ingreso> ingreso = ingresoService.obtenerIngresoPorId(id);
        return ingreso.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarIngreso(@PathVariable Integer id, @RequestBody Ingreso ingreso) {
        // Buscar el ingreso existente
        Optional<Ingreso> ingresoExistente = ingresoService.obtenerIngresoPorId(id);
        if (!ingresoExistente.isPresent()) {
            throw new ResourceNotFoundException("Ingreso no encontrado con ID: " + id);
        }

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Buscar al usuario autenticado
        Optional<Usuario> usuarioOptional = usuarioService.obtenerPorEmail(email);
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autorizado");
        }

        Ingreso ingresoDB = ingresoExistente.get();
        ingresoDB.setMonto(ingreso.getMonto() != null ? ingreso.getMonto() : ingresoDB.getMonto());
        ingresoDB.setConcepto(ingreso.getConcepto() != null ? ingreso.getConcepto() : ingresoDB.getConcepto());
        ingresoDB.setUsuario(usuarioOptional.get());
        ingresoDB.setFecha(LocalDateTime.now()); // Actualiza la fecha de modificación

        // Guardar los cambios
        Ingreso ingresoActualizado = ingresoService.actualizarIngreso(ingresoDB);

        return ResponseEntity.ok(ingresoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarIngreso(@PathVariable Integer id) {
        boolean eliminado = ingresoService.eliminarIngreso(id);
        if (eliminado) {
            return ResponseEntity.ok("Ingreso eliminado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingreso no encontrado");
        }
    }

}
