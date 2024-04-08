package com.proyecto_v1.proyecto_v1.repository;

import com.proyecto_v1.proyecto_v1.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.util.Optional;

public interface RolRepository extends JpaRepository<Roles, Long> {
    //Optional<Roles>finByName(String name);

}
