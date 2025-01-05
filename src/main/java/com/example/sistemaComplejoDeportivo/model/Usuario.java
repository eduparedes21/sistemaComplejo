package com.example.sistemaComplejoDeportivo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa un Usuario en el sistema. Existen dos roles principales:
 * - administrador - personal
 */
@Entity
@Getter
@Setter
@Table(name = "usuarios") // Nombre exacto de la tabla en la base de datos
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String contraseña;

    @Column(nullable = false, length = 20)
    private String rol; // Puede ser 'administrador' o 'personal'

    //constructor vacio
    public Usuario() {
    }

    /**
     * Constructor para facilitar la creación de usuarios.
     *
     * @param nombre Nombre del usuario
     * @param email Correo electrónico del usuario
     * @param contraseña Contraseña encriptada
     * @param rol Rol del usuario (administrador/personal)
     */
    public Usuario(String nombre, String email, String contraseña, String rol) {
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.rol = rol;
    }
}
