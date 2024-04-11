package com.proyecto_v1.proyecto_v1.controller;

import com.proyecto_v1.proyecto_v1.dto.entrada.LoginDtoEntrada;
import com.proyecto_v1.proyecto_v1.dto.entrada.UsuarioDtoEntrada;
import com.proyecto_v1.proyecto_v1.dto.salida.TokenDtoSalida;
import com.proyecto_v1.proyecto_v1.dto.salida.UsuarioDtoSalida;
import com.proyecto_v1.proyecto_v1.service.implementacion.InterfaceUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@AllArgsConstructor
public class AuthController {
    private final InterfaceUsuarioService interfaceUsuarioService;

    //=================================================================//
    @Operation(summary = "Crear  un nuevo usuario 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario 'ADMIN' Creado  correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDtoSalida.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })

    @PostMapping("/registrar/admin")
    public ResponseEntity<TokenDtoSalida> registrarUsuarioAdmin(@Valid @RequestBody UsuarioDtoEntrada usuario) {
        return new ResponseEntity<>(interfaceUsuarioService.crearUsuarioConRol(usuario, "ADMIN"), HttpStatus.CREATED);
    }

    //=================================================================//
    @Operation(summary = "Crear  un nuevo usuario 'USER'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario 'USER' Creado  correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDtoSalida.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PostMapping("/registrar/user")
    public ResponseEntity<TokenDtoSalida> registrarUsuarioUser(@Valid @RequestBody UsuarioDtoEntrada usuario) {
        return new ResponseEntity<>(interfaceUsuarioService.crearUsuarioConRol(usuario, "USER"), HttpStatus.CREATED);
    }

    //=================================================================//
    @Operation(summary = "Login usaurio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario logiado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenDtoSalida.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDtoSalida> loginUsuario(@Valid @RequestBody LoginDtoEntrada login) {
        return new ResponseEntity<>(interfaceUsuarioService.logearUsuario(login), HttpStatus.OK);
    }
}
