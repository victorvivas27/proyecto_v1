package com.proyecto_v1.proyecto_v1.service.implementacion;

import com.proyecto_v1.proyecto_v1.dto.entrada.LoginDtoEntrada;
import com.proyecto_v1.proyecto_v1.dto.entrada.UsuarioDtoEntrada;
import com.proyecto_v1.proyecto_v1.dto.modificar.UsuarioDtoModificar;
import com.proyecto_v1.proyecto_v1.dto.salida.TokenDtoSalida;
import com.proyecto_v1.proyecto_v1.dto.salida.UsuarioDtoSalida;

import java.util.List;

public interface InterfaceUsuarioService {
    TokenDtoSalida crearUsuarioConRol(UsuarioDtoEntrada usuarioDTOEntrada, String nombreRol);

    //UsuarioDtoSalida crearUsuarioAdmin(UsuarioDtoEntrada usuarioDTOEntrada);

    List<UsuarioDtoSalida> obtenerTodosLosUsuarios();

    UsuarioDtoSalida obtenerUsuarioPorId(Long idUsuario);

    UsuarioDtoSalida actualizarUsuario(UsuarioDtoModificar usuarioDTOModificar);

    void eliminarUsuario(Long idUsuario);

    TokenDtoSalida logearUsuario(LoginDtoEntrada loginDtoEntrada);
}
