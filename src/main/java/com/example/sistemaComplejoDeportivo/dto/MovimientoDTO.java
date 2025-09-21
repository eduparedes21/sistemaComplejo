package com.example.sistemaComplejoDeportivo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimientoDTO {
    private String descripcion;
    private String tipo; // INGRESO o EGRESO
    private Integer idArticulo;
    private Integer cantidad;
    private Double monto;
}
