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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

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
                .requestMatchers("/proveedores/**").hasRole("ADMIN") // Proteger proveedores para ADMIN
                .anyRequest().authenticated() // Requiere autenticación para el resto
                )
                .formLogin(login -> login
                .loginPage("/api/auth/login") // Ruta para mostrar el formulario de login
                .usernameParameter("email") // ← Asegura que email se use como username
                .passwordParameter("password") // ← Define el campo de la contraseña correctamente
                .defaultSuccessUrl("/dashboard", true) // Redirigir tras login exitoso
                .failureUrl("/api/auth/login?error=true") // Redirigir tras login fallido
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout") // Ruta para cerrar sesión
                .logoutSuccessUrl("/api/auth/login") // Redirigir tras logout
                )
                .exceptionHandling(ex -> ex
                .accessDeniedHandler(accessDeniedHandler()) // Manejar acceso denegado
                )
                .sessionManagement(session -> session
                .maximumSessions(1) // Solo permitir una sesión por usuario
                .maxSessionsPreventsLogin(false) // Permitir el nuevo login si se exceden las sesiones
                );
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/accessDenied");
        return accessDeniedHandler;
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
                .password(usuario.getPassword())
                .roles(usuario.getRol().equalsIgnoreCase("administrador") ? "ADMIN" : "USER")
                .build())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
