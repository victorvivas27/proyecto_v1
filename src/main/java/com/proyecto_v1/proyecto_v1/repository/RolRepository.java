package com.proyecto_v1.proyecto_v1.repository;

import com.proyecto_v1.proyecto_v1.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

}

