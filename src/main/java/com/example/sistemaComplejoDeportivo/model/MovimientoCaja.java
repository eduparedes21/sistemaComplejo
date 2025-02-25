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

    private String descripcion;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    private Double monto;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo; // INGRESO o EGRESO

    @ManyToOne
    @JoinColumn(name = "usuario_id_usuario", referencedColumnName = "id_usuario", nullable = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_articulo", referencedColumnName = "id_articulo", nullable = true)
    private Inventario inventario;

    private Integer cantidad; // 📌 Cantidad de producto vendido (si aplica)
}
