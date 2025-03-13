package com.example.teleconsultation.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    //@Value("${spring.security.jwt.secret}")
    private final String secret = "EqjWQ7MYqS4VkF2vZLNOOH9r4XVa3XrIsibni4cJ6eEsAphTKHvnMoRmZdfC0PLD";

    private final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

    public String generateToken(String email,Long id, String role) {
        //@Value("${spring.security.jwt.expiration}")
        long jwtExpiration = 86400000L;
        return Jwts.builder()
                .setSubject(email)
                .claim("id",id)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

}
