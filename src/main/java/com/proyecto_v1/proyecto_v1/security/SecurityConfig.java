package com.proyecto_v1.proyecto_v1.security;

import com.proyecto_v1.proyecto_v1.exception.InvalidTokenException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@NoArgsConstructor
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private InvalidTokenException invalidTokenException;

    /**
     * Configura el administrador de autenticación.
     *
     * @param authenticationConfiguration La configuración de autenticación.
     * @return El administrador de autenticación.
     * @throws Exception Si hay un error al obtener el administrador de autenticación.
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Define el bean para el PasswordEncoder.
     *
     * @return El PasswordEncoder.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define el bean para JwtAuthenticationFilter.
     *
     * @return JwtAuthenticationFilter.
     */
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Configura la cadena de filtros de seguridad.
     *
     * @param httpSecurity El objeto HttpSecurity para configurar la seguridad.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception Si hay un error al configurar la seguridad.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable() // Deshabilita CSRF
                .exceptionHandling() // Manejo de excepciones
                .authenticationEntryPoint((AuthenticationEntryPoint) invalidTokenException) // Configura el punto de entrada de autenticación
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura la política de creación de sesiones como STATELESS
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll() // Permite todas las solicitudes a /api/auth/**
                .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
                .and()
                .httpBasic(); // Configura la autenticación básica
        httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Agrega JwtAuthenticationFilter antes de UsernamePasswordAuthenticationFilter
        return httpSecurity.build(); // Retorna la cadena de filtros de seguridad configurada
    }
}
