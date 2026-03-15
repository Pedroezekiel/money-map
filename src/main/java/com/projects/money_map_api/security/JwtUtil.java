package com.projects.money_map_api.security;

import com.projects.money_map_api.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret:mySuperSecretKeyForJWT123456789!}")
    private String secret;

    @Value("${app.jwt.expiration:86400000}")
    private long expirationMs; // 24 hours

    /**
     * GENERATE TOKEN
     * Creates JWT token when user logs in
     *
     * @param user The authenticated user
     * @return JWT token string
     */
    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(user.getId())
                .claim("email", user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    /**
     * GET USER ID FROM TOKEN
     * Extracts user ID (subject) from token
     *
     * @param token JWT token
     * @return User ID as UUID
     */
    public String getUserIdFromToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token: " + e.getMessage());
        }
    }

    /**
     * GET EMAIL FROM TOKEN
     * Extracts email claim from token
     *
     * @param token JWT token
     * @return User's email
     */
    public String getEmailFromToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.get("email", String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token: " + e.getMessage());
        }
    }

    /**
     * VALIDATE TOKEN
     * Checks if token is valid and not expired
     *
     * @param token JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token: " + e.getMessage());
        }
    }

    /**
     * GET ALL CLAIMS FROM TOKEN
     * Private helper method to extract claims
     * Uses new parserBuilder() API (not deprecated)
     *
     * @param token JWT token
     * @return Claims object
     */
    private Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * CHECK IF TOKEN IS EXPIRED
     *
     * @param token JWT token
     * @return true if expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * EXTRACT TOKEN FROM HEADER
     * Removes "Bearer " prefix from Authorization header
     *
     * @param authHeader Authorization header value
     * @return token without "Bearer " prefix, or null
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}