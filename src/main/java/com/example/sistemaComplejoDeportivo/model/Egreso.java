package com.example.sistemaComplejoDeportivo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "egresos")
public class Egreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_egreso")
    private Integer idEgreso;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, length = 255)
    private String concepto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", foreignKey = @ForeignKey(name = "egresos_id_usuario_fkey"), nullable = true)
    private Usuario usuario;
}
