package com.projects.money_map_api.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * WHITELIST - Public Endpoints
 *
 * Manages endpoints that don't require JWT authentication
 * Used by JwtAuthenticationFilter to skip token validation
 *
 * Usage:
 * if (whitelist.isPublicPath(request)) {
 *     // Skip JWT validation
 * }
 */
@Component
public class Whitelist {

    /**
     * Public endpoint patterns
     * Paths that don't require JWT authentication
     */
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/verify",
            "/api/auth/refresh",
            "/health",
            "/health/live",
            "/health/ready",
            "/",
            "/swagger-ui.html",
            "/swagger-ui/",
            "/v3/api-docs",
            "/v3/api-docs/",
            "/actuator",
            "/actuator/"
    );



    public boolean isPublicPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        return PUBLIC_PATHS.contains(path);
    }

}