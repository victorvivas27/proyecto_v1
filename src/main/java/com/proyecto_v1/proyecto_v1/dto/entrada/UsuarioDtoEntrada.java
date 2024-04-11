package com.proyecto_v1.proyecto_v1.dto.entrada;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * UsuarioDtoEntrada es un DTO utilizado para recibir datos de entrada relacionados con un usuario.
 */

/**
 * DTO "Data Transfer Object" utilizado para recibir datos de entrada relacionados con un usuario.
 */
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UsuarioDtoEntrada {
    /*================================================================================================*/
    @NotBlank(message = "El campo 'nombre' no puede estar en blanco")
    @NotNull(message = "El campo 'nombre' no puede ser nulo")
    @Size(min = 3, max = 50, message = "El campo 'nombre' debe tener entre 3 y 50 caracteres")
    private String nombre;
    /*================================================================================================*/
    @NotBlank(message = "El campo 'email' no puede estar en blanco")
    @NotNull(message = "El campo 'email' no puede ser nulo")
    @Email(message = "Proporciona una dirección de correo electrónico válida.")
    @Size(max = 30, message = "El campo 'email' no puede superar los 30 caracteres.")
    private String email;
    /*================================================================================================*/
    @NotBlank(message = "El campo 'password' no puede estar en blanco")
    @NotNull(message = "El campo 'password' no puede ser nulo")
    @Size(min = 3, max = 15, message = "La contraseña debe tener entre 3 y 15 caracteres.")
    private String password;
    /*================================================================================================*/

}
