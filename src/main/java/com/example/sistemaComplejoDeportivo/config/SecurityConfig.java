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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/css/**", "/js/**").permitAll() // Permitir login y recursos estáticos
                .requestMatchers("/api/caja/**").authenticated()
                .requestMatchers("/caja/reportes").permitAll() // Permitir acceso a todos
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll() // APIs REST públicas

                .anyRequest().authenticated() // Requiere autenticación para el resto
                )
                .formLogin(login -> login
                .loginPage("/api/auth/login") // Ruta para mostrar el formulario de login
                .defaultSuccessUrl("/dashboard", true) // Redirigir tras login exitoso
                .failureUrl("/api/auth/login?error=true") // Redirigir tras login fallido
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout") // Ruta para cerrar sesión
                .logoutSuccessUrl("/api/auth/login") // Redirigir tras logout
                )
                .sessionManagement(session -> session
                .maximumSessions(1) // Solo permitir una sesión por usuario
                .maxSessionsPreventsLogin(false) // Permitir el nuevo login si se exceden las sesiones
                );
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
