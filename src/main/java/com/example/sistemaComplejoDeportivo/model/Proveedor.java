package com.example.sistemaComplejoDeportivo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(name = "nombre_empresa", nullable = false, length = 100)
    private String nombreEmpresa;

    @Column(nullable = true, length = 50)
    private String contacto;

    @Column(nullable = true, length = 20)
    private String telefono;

    @Column(nullable = true, length = 255)
    private String direccion;
}
