package com.example.sistemaComplejoDeportivo.model;

import jakarta.persistence.*;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;

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
    private String password;

    @Column(nullable = false, length = 20)
    private String rol; // Puede ser 'administrador' o 'personal'

    @Column(nullable = false, length = 20)
    private String estado; // Puede ser 'activo' o 'inactivo'

    //constructor vacio
    public Usuario() {
    }

    /**
     * Constructor para facilitar la creación de usuarios.
     *
     * @param nombre Nombre del usuario
     * @param email Correo electrónico del usuario
     * @param password Contraseña encriptada
     * @param rol Rol del usuario (administrador/personal)
     * * @param estado Estado del usuario (activo/inactivo)
     */
    public Usuario(String nombre, String email, String password, String rol, String estado) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }

    public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

        Optional<Usuario> findByEmail(String email);
    }

}
