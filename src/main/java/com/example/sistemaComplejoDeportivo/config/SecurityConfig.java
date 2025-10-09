package com.example.sistemaComplejoDeportivo.config;

import com.example.sistemaComplejoDeportivo.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final UsuarioRepository usuarioRepository;

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
                .csrf(csrf -> {
                })
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/css/**", "/js/**").permitAll()
                .requestMatchers("/api/caja/**").authenticated()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/proveedores/**").hasRole("ADMIN")
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                )
                .formLogin(login -> login
                .loginPage("/api/auth/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/api/auth/login?error=true")
                .failureHandler((request, response, exception) -> {
                    if (exception.getMessage().contains("inactiva")) {
                        response.sendRedirect("/api/auth/login?error=inactivo");
                    } else {
                        response.sendRedirect("/api/auth/login?error=true");
                    }
                })
                .permitAll()
                )
                .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .logoutSuccessUrl("/api/auth/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                )
                .exceptionHandling(ex -> ex
                .accessDeniedHandler(accessDeniedHandler())
                )
                .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                )
                .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
                .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000))
                .contentTypeOptions(contentType -> {
                })
                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.SAME_ORIGIN))
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
                .map(usuario -> {
                    if (usuario.getEstado().equalsIgnoreCase("inactivo")) {
                        throw new RuntimeException("Tu cuenta está inactiva. Contacta con el administrador.");
                    }
                    return User.builder()
                            .username(usuario.getEmail())
                            .password(usuario.getPassword())
                            .roles(usuario.getRol().equalsIgnoreCase("administrador") ? "ADMIN" : "USER")
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos."));
    }
}
