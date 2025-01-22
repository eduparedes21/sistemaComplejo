package com.example.sistemaComplejoDeportivo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "miClaveSecretaParaJWT"; // Cambia esto por una clave segura

    // Generar el token
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Obtener claims del token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Validar el token
    public boolean validateToken(String token, String email) {
        final String tokenEmail = extractClaims(token).getSubject();
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
