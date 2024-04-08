package com.proyecto_v1.proyecto_v1.repository;

import com.proyecto_v1.proyecto_v1.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> finByEmail(String email);

    //Boolean existsByEamil(String email);
}
