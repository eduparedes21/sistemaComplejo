package com.example.sistemaComplejoDeportivo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "canchas")
public class Canchas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String tipo;
    
    @Column(nullable = false, length = 50)
    private String estado; // Disponible, Ocupada, Mantenimiento

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    private Double precioHora;

    
}
