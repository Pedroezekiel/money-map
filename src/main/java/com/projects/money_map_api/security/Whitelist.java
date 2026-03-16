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
 * Used by SecurityConfig to allow public access
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
    public static final List<String> PUBLIC_PATHS = Arrays.asList(
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
            "/v3/api-docs/",
            "/actuator",
            "/actuator/",
            "/swagger-ui.html",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/index.html",
            "/v3/api-docs",
            "/swagger-resources",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security",
            "/webjars/swagger-ui"
    );

    /**
     * Public endpoint prefixes
     * Any path starting with these doesn't require authentication
     */
    public static final List<String> PUBLIC_PREFIXES = Arrays.asList(
            "/api/auth/",
            "/health",
            "/swagger-ui",
            "/v3/api-docs",
            "/webjars/swagger-ui",
            "/swagger-resources",
            "/actuator"
    );

    /**
     * CHECK IF PATH IS PUBLIC
     *
     * @param request The HTTP request
     * @return true if path doesn't need JWT, false if it does
     */
    public boolean isPublicPath(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Check exact matches
        if (PUBLIC_PATHS.contains(path)) {
            return true;
        }

        // Check prefix matches
        return PUBLIC_PREFIXES.stream()
                .anyMatch(path::startsWith);
    }

    /**
     * CHECK IF PATH IS PROTECTED (needs JWT)
     *
     * @param request The HTTP request
     * @return true if path requires JWT, false if it doesn't
     */
    public boolean isProtectedPath(HttpServletRequest request) {
        return !isPublicPath(request);
    }

    /**
     * GET ALL PUBLIC PATHS
     * Used by SecurityConfig
     */
    public List<String> getPublicPaths() {
        return PUBLIC_PATHS;
    }

    /**
     * GET ALL PUBLIC PREFIXES
     * Used by SecurityConfig
     */
    public List<String> getPublicPrefixes() {
        return PUBLIC_PREFIXES;
    }

    /**
     * ADD PATH TO WHITELIST (Runtime)
     * Useful for dynamic whitelist management
     */
    public void addPublicPath(String path) {
        if (!PUBLIC_PATHS.contains(path)) {
            PUBLIC_PATHS.add(path);
        }
    }

    /**
     * REMOVE PATH FROM WHITELIST (Runtime)
     */
    public void removePublicPath(String path) {
        PUBLIC_PATHS.remove(path);
    }
}