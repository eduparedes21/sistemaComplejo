package com.example.sistemaComplejoDeportivo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "inventario") // Nombre exacto de la tabla en la base de datos
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_articulo")
    private Integer idArticulo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "cantidad_stock", nullable = false, columnDefinition = "integer default 0")
    private Integer cantidadStock;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "id_proveedor")
    private Integer idProveedor;

    public Inventario() {
        // Constructor vacío requerido por JPA
    }

    public Inventario(String nombre, String descripcion, Integer cantidadStock, BigDecimal precioUnitario, Integer idProveedor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadStock = cantidadStock;
        this.precioUnitario = precioUnitario;
        this.idProveedor = idProveedor;
    }
}
