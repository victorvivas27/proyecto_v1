package com.proyecto_v1.proyecto_v1.dto.salida;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO utilizado para representar el token de autenticación en la salida.
 */
@Data

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class TokenDtoSalida {
    private String token;
    private String tokenType = "Bearer ";

    public TokenDtoSalida(String token) {
        this.token = token; // Asignar el token al atributo correspondiente
    }
}
