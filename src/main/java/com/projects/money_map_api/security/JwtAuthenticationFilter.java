package com.projects.money_map_api.security;

import com.projects.money_map_api.entity.User;
import com.projects.money_map_api.exception.MoneyMapException;
import com.projects.money_map_api.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
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
            String authHeader = request.getHeader("Authorization");
            String token = jwtUtil.extractTokenFromHeader(authHeader);
            if (token != null && jwtUtil.validateToken(token)) {
                String userId = jwtUtil.getUserIdFromToken(token);
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null);
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            throw new MoneyMapException("Authentication failed: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return whitelist.isPublicPath(request);
    }
}