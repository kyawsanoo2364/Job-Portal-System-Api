package com.javadev.jobportal.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {
    private final SecretKey secret_key;
    private final Long expiration;

    public JwtUtils(@Value("${security.jwt.expiration}") Long expiration
    ,@Value("${security.jwt.secret}") String secret_key
    ) {
        if(secret_key==null || secret_key.length() < 32) {
            throw new IllegalArgumentException("Secret key length should be at least 32 characters");
        }
        this.secret_key = Keys.hmacShaKeyFor(secret_key.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claim("roles", userDetails.getAuthorities())
                .signWith(secret_key)
                .subject(userDetails.getUsername())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(secret_key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().verifyWith(secret_key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
