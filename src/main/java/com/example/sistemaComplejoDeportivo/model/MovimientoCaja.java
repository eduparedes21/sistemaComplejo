package com.example.sistemaComplejoDeportivo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "movimiento_caja")
public class MovimientoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoria;
    private String descripcion;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    private Double monto;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo; // INGRESO o EGRESO

    @ManyToOne
    @JoinColumn(name = "usuario_id_usuario") // Coincide con la columna en la base de datos
    private Usuario usuario;

}
