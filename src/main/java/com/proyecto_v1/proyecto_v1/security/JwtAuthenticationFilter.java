package com.proyecto_v1.proyecto_v1.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro de autenticación JWT que se ejecuta una vez por cada solicitud.
 * Este filtro verifica la validez del token JWT en la solicitud y autentica al usuario si el token es válido.
 */
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private CustomUserDetailsService customUserDetailsService;
    private JwtToken jwtToken;

    /**
     * Obtiene el token JWT de la cabecera de autorización de la solicitud.
     *
     * @param request La solicitud HTTP.
     * @return El token JWT si está presente en la cabecera de autorización, de lo contrario, null.
     */
    public String obtenerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    /**
     * Filtra las solicitudes HTTP para autenticar a los usuarios utilizando tokens JWT válidos.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param filterChain La cadena de filtros para procesar la solicitud.
     * @throws ServletException Si se produce un error de servlet.
     * @throws IOException      Si se produce un error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = obtenerToken(request);
        if (StringUtils.hasText(token) && jwtToken.validarToken(token)) {
            String email = jwtToken.obtenerEmailDeJwt(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            List<String> userRoles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).toList();
            if (userRoles.contains("USER") || userRoles.contains("ADMIN")) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
