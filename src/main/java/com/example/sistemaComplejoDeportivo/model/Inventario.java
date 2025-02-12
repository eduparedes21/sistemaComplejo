package com.example.sistemaComplejoDeportivo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private Integer idArticulo;

    @Column(nullable = false, length = 100)
    @JsonProperty("nombre")
    private String nombre;

    @Column(nullable = false)
    @JsonProperty("descripcion")
    private String descripcion;

    @Column(name = "cantidad_stock", nullable = false, columnDefinition = "integer default 0")
    @JsonProperty("cantidad")
    private Integer cantidadStock;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    @JsonProperty("precioUnitario")
    private BigDecimal precioUnitario;

    @Column(name = "categoria")
    @JsonProperty("categoria")
    private String categoria; // Nuevo campo

    @ManyToOne
    @JoinColumn(name = "id_proveedor", referencedColumnName = "id_proveedor", nullable = true)
    @JsonProperty("proveedor")
    private Proveedor proveedor;

    public Inventario() {
        // Constructor vacío requerido por JPA
    }

    public Inventario(String nombre, String descripcion, Integer cantidadStock, BigDecimal precioUnitario, Proveedor idProveedor, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadStock = cantidadStock;
        this.precioUnitario = precioUnitario;
        this.proveedor = idProveedor;
        this.categoria = categoria;
    }
}
