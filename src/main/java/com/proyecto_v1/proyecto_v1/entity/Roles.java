package com.proyecto_v1.proyecto_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase Roles representa los roles de usuarios en el sistema.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")

public class Roles {
    /*==========================================================*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_roles")
    private Long idRol;
    /*=========================================================*/
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;
    /*=========================================================*/


    public Roles(String nombreRol) {
        this.nombre = nombreRol;
    }
}
