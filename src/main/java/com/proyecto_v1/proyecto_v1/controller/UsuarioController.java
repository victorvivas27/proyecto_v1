package com.proyecto_v1.proyecto_v1.controller;

import com.proyecto_v1.proyecto_v1.dto.modificar.UsuarioDtoModificar;
import com.proyecto_v1.proyecto_v1.dto.salida.UsuarioDtoSalida;
import com.proyecto_v1.proyecto_v1.service.implementacion.InterfaceUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin
@AllArgsConstructor
public class UsuarioController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);
    private final InterfaceUsuarioService interfaceUsuarioService;

    //=============================================================================//
    @Operation(summary = "Obtener la lista de todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDtoSalida.class))}),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    //@PreAuthorize("hasRole('USER')")
    //@PreAuthorize("hasRole")
    //@PreAuthorize("hasAnyRole")
    //@PreAuthorize("hasAnyRole('USER')")
    //@PreAuthorize("hasAuthority")
    //@PreAuthorize("hasAnyAuthority('ADMIN')")
    //@PreAuthorize("hasAnyAuthority")

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDtoSalida>> listarUsuarios() {
        return new ResponseEntity<>(interfaceUsuarioService.obtenerTodosLosUsuarios(), HttpStatus.OK);
    }

    //===========================================================================//
    @Operation(summary = "Obtener un usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDtoSalida.class))}),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping("/buscar/{idUsuario}")
    public ResponseEntity<UsuarioDtoSalida> obtenerUsuarioPorId(@Parameter(description = "ID del usuario a obtener", required = true)
                                                                @PathVariable Long idUsuario) {
        return new ResponseEntity<>(interfaceUsuarioService.obtenerUsuarioPorId(idUsuario), HttpStatus.OK);
    }

    //==========================================================================================//
    @Operation(summary = "Actualizar un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDtoSalida.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @PutMapping("/modificar")
    public ResponseEntity<UsuarioDtoSalida> actualizarUsuario(@Valid @RequestBody UsuarioDtoModificar usuario) {
        return new ResponseEntity<>(interfaceUsuarioService.actualizarUsuario(usuario), HttpStatus.OK);
    }

    //============================================================================================//
    @Operation(summary = "Eliminar un usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @DeleteMapping("/eliminar/{idUsuario}")
    public ResponseEntity<?> eliminarUsuario(@Parameter(description = "ID del usuario a eliminar", required = true)
                                             @PathVariable Long idUsuario) {
        interfaceUsuarioService.eliminarUsuario(idUsuario);
        return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
    }
}
