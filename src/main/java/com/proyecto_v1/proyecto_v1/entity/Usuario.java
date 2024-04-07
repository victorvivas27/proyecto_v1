package com.proyecto_v1.proyecto_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Column(length = 100, nullable = false, name = "nombre")
    private String nombre;
    @Column(length = 100, unique = true, nullable = false, name = "email")
    private String email;
    @Column(length = 100, nullable = false, name = "password")
    private String password;
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Roles.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id_roles")
    )
    private Set<Roles> roles;
}
