package com.proyecto_v1.proyecto_v1.security;

import com.proyecto_v1.proyecto_v1.dto.entrada.LoginDtoEntrada;
import com.proyecto_v1.proyecto_v1.dto.salida.TokenDtoSalida;
import com.proyecto_v1.proyecto_v1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Método para realizar el inicio de sesión.
     *
     * @param loginDtoEntrada Objeto DTO que contiene las credenciales de inicio de sesión (email y contraseña).
     * @return Objeto DTO que contiene el token generado en caso de inicio de sesión exitoso.
     * @throws AccessDeniedException Si el usuario no tiene los permisos requeridos para iniciar sesión.
     */
    public TokenDtoSalida login(LoginDtoEntrada loginDtoEntrada) {
        // Autenticar las credenciales del usuario
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDtoEntrada.getEmail(),
                loginDtoEntrada.getPassword()));

        // Obtener los detalles del usuario
        UserDetails userDetails = usuarioRepository.findByEmail(loginDtoEntrada.getEmail()).orElseThrow();

        // Verificar si el usuario tiene permisos de ADMIN o USER para generar el token
        if (userDetails.getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("ADMIN") || a.getAuthority().equals("USER"))) {
            // Generar el token utilizando JwtService
            String token = jwtService.generateToken(userDetails);
            return TokenDtoSalida.builder().token(token).build();
        } else {
            // Lanzar excepción si el usuario no tiene permisos suficientes
            throw new AccessDeniedException("No tiene permisos de administrador");
        }
    }
}
