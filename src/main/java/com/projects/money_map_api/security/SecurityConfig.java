package com.projects.money_map_api.config;

import com.projects.money_map_api.security.JwtAuthenticationFilter;
import com.projects.money_map_api.security.Whitelist;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * SPRING SECURITY CONFIGURATION
 *
 * Configures:
 * - JWT authentication filter
 * - Authorization rules (using Whitelist)
 * - CORS
 * - Password encoding
 * - Session management
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Whitelist whitelist;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, Whitelist whitelist) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.whitelist = whitelist;
    }

    /**
     * PASSWORD ENCODER
     *
     * Uses BCrypt for secure password hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * SECURITY FILTER CHAIN
     *
     * Configures:
     * - Which endpoints are public (from Whitelist)
     * - Which endpoints require authentication
     * - JWT filter registration
     * - CORS settings
     * - Session management
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Get public paths and prefixes from Whitelist
        String[] publicPaths = whitelist.getPublicPaths().toArray(new String[0]);
        String[] publicPrefixes = whitelist.getPublicPrefixes().toArray(new String[0]);

        http
                // CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF - disabled for stateless JWT auth
                .csrf(csrf -> csrf.disable())

                // Session management - stateless (no sessions)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Authorization rules - using Whitelist
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (from Whitelist) - no auth required
                        .requestMatchers(publicPaths).permitAll()
                        .requestMatchers(publicPrefixes).permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // Add JWT filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS CONFIGURATION
     *
     * Allows requests from:
     * - localhost:3000 (React frontend)
     * - localhost:4200 (Angular frontend)
     * - Other configured origins
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed origins
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:4200",
                "http://localhost:8080"
        ));

        // Allowed methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Allowed headers
        configuration.setAllowedHeaders(Arrays.asList(
                "Content-Type",
                "Authorization",
                "X-Requested-With"
        ));

        // Allow credentials
        configuration.setAllowCredentials(true);

        // Max age
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}