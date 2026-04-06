package com.projects.money_map_api.controller;

import com.projects.money_map_api.dto.request.LoginRequest;
import com.projects.money_map_api.dto.request.RegisterRequest;
import com.projects.money_map_api.dto.request.ResetPasswordRequest;
import com.projects.money_map_api.dto.request.ForgotPasswordRequest;
import com.projects.money_map_api.dto.response.AuthResponse;
import com.projects.money_map_api.dto.response.BaseResponse;
import com.projects.money_map_api.entity.User;
import com.projects.money_map_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create new user account with email and password")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.ok(201, "User registered successfully", response));
    }

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


    @PostMapping("/forgot-password")
    @Operation(
            summary = "Forgot password",
            description = "Request password reset (currently returns message, future: sends reset email)"
    )
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {

        String response = authService.forgotPassword(request);

        return ResponseEntity.ok(
                BaseResponse.ok(200, "Password reset link sent to email", response)
        );
    }


//    @GetMapping("/me")
//    @Operation(summary = "Get current user", description = "Get authenticated user information")
//    public ResponseEntity<?> getCurrentUser() {
//
//        // Get current authenticated user
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) authentication.getPrincipal();
//
//        // Build response
//        AuthResponse authResponse = AuthResponse.builder()
//                .userId(currentUser.getId())
//                .email(currentUser.getEmail())
//                .name(currentUser.getName())
//                .role(currentUser.getRole().name())
//                .build();
//
//        return ResponseEntity.ok(
//                BaseResponse.ok(200, "User retrieved successfully", authResponse)
//        );
//    }
}