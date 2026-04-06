package com.projects.money_map_api.security;

import com.projects.money_map_api.entity.User;
import com.projects.money_map_api.exception.MoneyMapException;
import com.projects.money_map_api.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final Whitelist whitelist;


    @Override
    protected void doFilterInternal(
            @NonNull  HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            log.info("Processing authentication for request: {} {}", request.getMethod(), request.getRequestURI());
            String authHeader = request.getHeader("Authorization");
            log.info("Authorization header received: {}", authHeader);
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            log.info("Token received: {}", token);
            if (token != null && jwtUtil.validateToken(token)) {
                log.info("Token is valid. Extracting user information.");
                String userId = jwtUtil.getUserIdFromToken(token);
                log.info("User information extracted 1: {}", userId);
                User user = userRepository.findById(userId).orElse(null);
                log.info("User information extracted 2: {}", user);
                if (user != null) {
                    log.info("User information extracted: {}", user);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, List.of());
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
                log.info("User information extracted 3: {}", user);
            }
            log.info("Authentication complete.");
        } catch (Exception e) {
            throw new MoneyMapException("Authentication failed: " + e.getMessage());
        }
        log.info("Continuing filter chain for request: {} {}", request.getMethod(), request.getRequestURI());
        filterChain.doFilter(request, response);
        log.info("Filter chain complete.");
    }


    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return whitelist.isPublicPath(request);
    }
}