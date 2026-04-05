package com.projects.money_map_api.controller;

import com.projects.money_map_api.dto.request.LoginRequest;
import com.projects.money_map_api.dto.request.RegisterRequest;
import com.projects.money_map_api.dto.request.ResetPasswordRequest;
import com.projects.money_map_api.dto.request.ForgotPasswordRequest;
import com.projects.money_map_api.dto.response.AuthResponse;
import com.projects.money_map_api.dto.response.BaseResponse;
import com.projects.money_map_api.entity.User;
import com.projects.money_map_api.exception.MoneyMapException;
import com.projects.money_map_api.repository.UserRepository;
import com.projects.money_map_api.security.JwtUtil;
import com.projects.money_map_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * AUTH CONTROLLER
 *
 * Handles user authentication:
 * - Register new users
 * - Login users
 * - Reset password (authenticated)
 * - Forgot password (public)
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * REGISTER - Create new user account
     *
     * POST /api/auth/register
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create new user account with email and password")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.ok(201, "User registered successfully", response));
    }

    /**
     * LOGIN - Authenticate user and get JWT token
     *
     * POST /api/auth/login
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and get JWT token")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        // Build response
        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.ok(
                BaseResponse.ok(200, "Login successful", authResponse)
        );
    }


    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Change password for authenticated user")
    public ResponseEntity<?> resetPassword(@AuthenticationPrincipal User user, @Valid @RequestBody ResetPasswordRequest request) {
        String response = authService.resetPassword(user, request);
        return ResponseEntity.ok(
                BaseResponse.ok(200, "Password reset successfully", response)
        );
    }

    /**
     * FORGOT PASSWORD - Request password reset (public endpoint)
     *
     * POST /api/auth/forgot-password
     * No authentication required
     *
     * Note: Currently returns message only
     * Future: Send reset email with token
     */
    @PostMapping("/forgot-password")
    @Operation(
            summary = "Forgot password",
            description = "Request password reset (currently returns message, future: sends reset email)"
    )
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {

        // Check if user exists
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new MoneyMapException(
                        "No account found with this email",
                        HttpStatus.NOT_FOUND
                ));

        // Future: Generate password reset token and send email
        // For now: Just return message

        MessageResponse messageResponse = MessageResponse.builder()
                .message("If an account exists with this email, you will receive a password reset link")
                .success(true)
                .build();

        return ResponseEntity.ok(
                BaseResponse.ok(200, "Password reset link sent to email", messageResponse)
        );
    }

    /**
     * GET CURRENT USER - Get authenticated user info
     *
     * GET /api/auth/me
     * Requires: JWT token in Authorization header
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get authenticated user information")
    public ResponseEntity<?> getCurrentUser() {

        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        // Build response
        AuthResponse authResponse = AuthResponse.builder()
                .userId(currentUser.getId())
                .email(currentUser.getEmail())
                .name(currentUser.getName())
                .role(currentUser.getRole().name())
                .build();

        return ResponseEntity.ok(
                BaseResponse.ok(200, "User retrieved successfully", authResponse)
        );
    }
}