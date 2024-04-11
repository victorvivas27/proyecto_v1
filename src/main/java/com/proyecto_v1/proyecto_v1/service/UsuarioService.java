package com.proyecto_v1.proyecto_v1.service;

import com.proyecto_v1.proyecto_v1.dto.entrada.LoginDtoEntrada;
import com.proyecto_v1.proyecto_v1.dto.entrada.UsuarioDtoEntrada;
import com.proyecto_v1.proyecto_v1.dto.modificar.UsuarioDtoModificar;
import com.proyecto_v1.proyecto_v1.dto.salida.TokenDtoSalida;
import com.proyecto_v1.proyecto_v1.dto.salida.UsuarioDtoSalida;
import com.proyecto_v1.proyecto_v1.entity.Roles;
import com.proyecto_v1.proyecto_v1.entity.Usuario;
import com.proyecto_v1.proyecto_v1.exception.BadRequestException;
import com.proyecto_v1.proyecto_v1.exception.ResourceNotFoundException;
import com.proyecto_v1.proyecto_v1.repository.RolRepository;
import com.proyecto_v1.proyecto_v1.repository.UsuarioRepository;
import com.proyecto_v1.proyecto_v1.security.JwtService;
import com.proyecto_v1.proyecto_v1.service.implementacion.InterfaceUsuarioService;
import com.proyecto_v1.proyecto_v1.util.JsonPrinter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService implements InterfaceUsuarioService {
    private final Logger LOGGER = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Transactional
    public TokenDtoSalida crearUsuarioConRol(UsuarioDtoEntrada usuarioDTOEntrada, String nombreRol) {
        // Convertir UsuarioDtoEntrada a Usuario
        Usuario usuarioEntidad = modelMapper.map(usuarioDTOEntrada, Usuario.class);

        // Encriptar la contraseña
        String contraseñaEncriptada = passwordEncoder.encode(usuarioEntidad.getPassword());
        usuarioEntidad.setPassword(contraseñaEncriptada);

        // Obtener el rol asociado al usuario o crearlo si no existe
        Roles rol = rolRepository.findByNombre(nombreRol)
                .orElseGet(() -> rolRepository.save(new Roles(nombreRol)));

        // Asignar el rol al usuario
        usuarioEntidad.setRoles(Collections.singleton(rol));

        // Persistir el usuario y obtener la entidad persistida
        Usuario usuarioAPersistir = usuarioRepository.save(usuarioEntidad);

        // Generar el token JWT utilizando el servicio de autenticación
        String jwt = jwtService.generateToken(usuarioAPersistir);

        // Crear el objeto TokenDtoSalida con el token generado
        TokenDtoSalida tokenDtoSalida = new TokenDtoSalida(jwt, "Bearer ");

        return tokenDtoSalida;
    }

    /*===================================================================================================================*/
    @Override
    public TokenDtoSalida logearUsuario(LoginDtoEntrada loginDtoEntrada) {
        // Autenticar al usuario utilizando el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDtoEntrada.getEmail(), loginDtoEntrada.getPassword())
        );

        // Establecer la información de autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Obtener el usuario autenticado (UserDetails)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generar el token JWT utilizando el JwtTokenProvider
        String token = jwtService.generateToken(userDetails);

        // Crear el objeto TokenDtoSalida con el token generado
        TokenDtoSalida tokenDtoSalida = new TokenDtoSalida(token, "Bearer ");

        return tokenDtoSalida;
    }

    /*===================================================================================================================*/
    @Override

    public List<UsuarioDtoSalida> obtenerTodosLosUsuarios() {
        List<UsuarioDtoSalida> usuariosDTOSalidas = usuarioRepository.findAll().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDtoSalida.class)).toList();

        LOGGER.info("Listado de todos los 'usuarios': {}", JsonPrinter.toString(usuariosDTOSalidas));

        return usuariosDTOSalidas;
    }

    /*===================================================================================================================*/
    @Override
    public UsuarioDtoSalida obtenerUsuarioPorId(Long idUsuario) {
        Usuario usuarioBuscado = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuarioBuscado != null) {
            return modelMapper.map(usuarioBuscado, UsuarioDtoSalida.class);
        } else {
            LOGGER.error("El id {} no se encuentra registrado en la base de datos", idUsuario);
            throw new BadRequestException("No se encontró el 'Usuario' con el ID: " + idUsuario);
        }
    }
    /*===================================================================================================================*/

    @Override
    public UsuarioDtoSalida actualizarUsuario(UsuarioDtoModificar usuario) {
        // Convertir UsuarioDtoModificar a Usuario
        Usuario usuarioRecibida = modelMapper.map(usuario, Usuario.class);

        // Obtener el usuario a actualizar de la base de datos
        Usuario usuarioAActualizar = usuarioRepository.findById(usuario.getIdUsuario()).orElse(null);
        UsuarioDtoSalida usuarioDTOSalida = null;

        if (usuarioAActualizar != null) {
            // Actualizar los datos del usuario excepto la contraseña
            usuarioAActualizar.setNombre(usuarioRecibida.getNombre());
            usuarioAActualizar.setEmail(usuarioRecibida.getEmail());
            //usuarioAActualizar.setRoles(usuarioRecibida.getRoles());

            // Si se proporciona una nueva contraseña, encriptarla y actualizarla
            if (!usuario.getPassword().isEmpty()) {
                String contraseñaEncriptada = passwordEncoder.encode(usuario.getPassword());
                usuarioAActualizar.setPassword(contraseñaEncriptada);
            }

            // Guardar el usuario actualizado en la base de datos
            usuarioRepository.save(usuarioAActualizar);

            // Convertir la entidad actualizada a UsuarioDtoSalida
            usuarioDTOSalida = modelMapper.map(usuarioAActualizar, UsuarioDtoSalida.class);

            LOGGER.warn("Usuario actualizado: {}", JsonPrinter.toString(usuarioDTOSalida));

        } else {
            LOGGER.error("No fue posible actualizar los datos ya que el 'Usuario' no se encuentra registrado");
            throw new BadRequestException("No fue posible actualizar los datos ya que el 'Usuario' no se encuentra registrado");
        }
        return usuarioDTOSalida;
    }

    /*===================================================================================================================*/
    @Override
    public void eliminarUsuario(Long idUsuario) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.getRoles().clear(); // Eliminar todas las relaciones con roles
            usuarioRepository.save(usuario); // Guardar para aplicar los cambios a la base de datos
            usuarioRepository.deleteById(idUsuario); // Eliminar al usuario después de eliminar las relaciones
            LOGGER.warn("Se ha eliminado el 'Usuario' con id_usuario: {}", idUsuario);

        } else {
            LOGGER.error("No se ha encontrado el 'Usuario' con id_usuario {}", idUsuario);
            throw new ResourceNotFoundException("No se ha encontrado el 'Usuario' con id " + idUsuario);
        }
    }

    /*===================================================================================================================*/
    @Transactional
    public TokenDtoSalida crearUsuarioAdmin(UsuarioDtoEntrada usuarioDTOEntrada) {
        return crearUsuarioConRol(usuarioDTOEntrada, "ADMIN");
    }

    @Transactional
    public TokenDtoSalida crearUsuarioUser(UsuarioDtoEntrada usuarioDTOEntrada) {
        return crearUsuarioConRol(usuarioDTOEntrada, "USER");
    }


}
