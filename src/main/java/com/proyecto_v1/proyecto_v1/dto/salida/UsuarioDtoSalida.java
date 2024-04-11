package com.proyecto_v1.proyecto_v1.dto.salida;

import com.proyecto_v1.proyecto_v1.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDtoSalida {
    private Long idUsuario;
    private String nombre;
    private String email;
    private String password;
    private Set<Roles> roles = new HashSet<>();
}
