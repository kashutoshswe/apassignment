package com.assignment.apassignment.utility;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.secret}")
    private String secretKey;

    // Method to get a secure SecretKey for HMAC-SHA256
    private SecretKey getSigningKey() {
        // Use Keys.secretKeyFor to generate a secure key with a length appropriate for HS256
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));  // Key generated from your secret string
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 1 hour
                .signWith(getSigningKey()) // Use SecretKey and algorithm
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    // Method to validate the token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);

        // Check if the username matches and if the token is not expired
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}