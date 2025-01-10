package com.example.sistemaComplejoDeportivo.config;

import com.example.sistemaComplejoDeportivo.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UsuarioRepository usuarioRepository;

    // Constructor para inyectar UsuarioRepository
    public SecurityConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para APIs REST
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login").permitAll() // Permitir acceso público al login
                .requestMatchers("/api/auth/admin/**").hasAuthority("administrador") // Restringir admin
                .requestMatchers("/api/inventario/eliminar/**").hasAuthority("administrador")
                .requestMatchers("/api/inventario/**").hasAnyAuthority("administrador", "personal")
                .anyRequest().authenticated() // El resto requiere autenticación
                )
                .sessionManagement(session -> session
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
                ) // Política de sesión Stateless (sin mantener estado de sesión)
                .authenticationProvider(authenticationProvider()) // Usar autenticación personalizada
                .httpBasic(); // Mantener HTTP Basic para otros endpoints si es necesario

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> usuarioRepository.findByEmail(email)
                .map(usuario -> User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword()) // Password encriptada
                .authorities(usuario.getRol()) // Autoridad basada en rol
                .build())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
