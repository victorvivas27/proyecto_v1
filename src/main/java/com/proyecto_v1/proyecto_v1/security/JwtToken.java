package com.proyecto_v1.proyecto_v1.security;

import com.proyecto_v1.proyecto_v1.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtToken {
    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${security.jwt.expiration-minutes}")
    private long EXPIRATION_MINUTES;

    /**
     * Genera un token JWT basado en la autenticación proporcionada.
     *
     * @param authentication La autenticación del usuario.
     * @return El token JWT generado.
     */
    public String generarToken(Authentication authentication) {
        String email = authentication.getName();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + (EXPIRATION_MINUTES * 60 * 1000));

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .setSubject(email)
                .setIssuer("YourIssuerName") // Agregar el emisor
                .setId(UUID.randomUUID().toString()) // Generar un ID único para el token
                .claim("roles", roles) // Agregar roles como una reclamación personalizada
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /**
     * Obtiene el email del usuario a partir de un token JWT.
     *
     * @param token El token JWT.
     * @return El email del usuario.
     */
    public String obtenerEmailDeJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Valida la autenticidad y vigencia de un token JWT.
     *
     * @param token El token JWT a validar.
     * @return true si el token es válido, false de lo contrario.
     * @throws InvalidTokenException Si el token es inválido o ha expirado.
     */
    public boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new InvalidTokenException("Token expired");
        } catch (Exception ex) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}