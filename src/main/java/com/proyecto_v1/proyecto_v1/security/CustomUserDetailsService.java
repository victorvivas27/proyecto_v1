package com.proyecto_v1.proyecto_v1.security;

import com.proyecto_v1.proyecto_v1.entity.Roles;
import com.proyecto_v1.proyecto_v1.entity.Usuario;
import com.proyecto_v1.proyecto_v1.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UsuarioRepository usuarioRepository;

    public Collection<GrantedAuthority> mapToAuthorities(List<Roles> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.finByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(usuario.getEmail(), usuario.getPassword(), mapToAuthorities((List<Roles>) usuario.getRoles()));
    }
}
