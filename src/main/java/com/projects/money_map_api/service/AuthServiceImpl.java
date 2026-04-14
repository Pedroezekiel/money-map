package com.projects.money_map_api.service;

import com.projects.money_map_api.dto.request.LoginRequest;
import com.projects.money_map_api.dto.request.RegisterRequest;
import com.projects.money_map_api.dto.request.ResetPasswordRequest;
import com.projects.money_map_api.dto.request.ForgotPasswordRequest;
import com.projects.money_map_api.dto.response.AuthResponse;
import com.projects.money_map_api.entity.User;
import com.projects.money_map_api.exception.MoneyMapException;
import com.projects.money_map_api.repository.UserRepository;
import com.projects.money_map_api.security.JwtUtil;
import com.projects.money_map_api.utils.ErrorMessage;
import com.projects.money_map_api.utils.SuccessMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Transactional
    @Override
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            log.warn("Password mismatch for registration attempt: {}", request.getEmail());
            throw new MoneyMapException(ErrorMessage.PASSWORD_DO_NOT_MATCH, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration attempt with existing email: {}", request.getEmail());
            throw new MoneyMapException(ErrorMessage.EMAIL_IS_ALREADY_USED, HttpStatus.CONFLICT);
        }

        validatePasswordStrength(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // Save user
        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getEmail());

        // Generate JWT token
        String token = jwtUtil.generateToken(user);

        // Return response
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Transactional
    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            throw new MoneyMapException(ErrorMessage.INVALID_LOGIN_CREDENTIALS, HttpStatus.UNAUTHORIZED);
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed: Invalid password for email: {}", request.getEmail());
            throw new MoneyMapException(ErrorMessage.INVALID_LOGIN_CREDENTIALS, HttpStatus.UNAUTHORIZED);
        }

        if (!user.isActive()) {
            log.warn("Login failed: User account inactive: {}", request.getEmail());
            throw new MoneyMapException(ErrorMessage.USER_IS_INACTIVE, HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(user);
        log.info("User logged in successfully: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Transactional
    @Override
    public String resetPassword(User user, ResetPasswordRequest request) {
        log.info("Reset password attempt for user: {}", user.getEmail());

        // Check if new passwords match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            log.warn("Reset password failed: Passwords don't match for user: {}", user.getEmail());
            throw new MoneyMapException(ErrorMessage.NEW_PASSWORDS_DO_NOT_MATCH, HttpStatus.BAD_REQUEST);
        }

        // Verify old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            log.warn("Reset password failed: Invalid old password for user: {}", user.getEmail());
            throw new MoneyMapException(ErrorMessage.OLD_PASSWORD_IS_INCORRECT, HttpStatus.UNAUTHORIZED);
        }

        // Check if new password is different from old
        if (request.getOldPassword().equals(request.getNewPassword())) {
            log.warn("Reset password failed: New password same as old for user: {}", user.getEmail());
            throw new MoneyMapException(ErrorMessage.NEW_PASSWORD_MUST_BE_DIFFERENT, HttpStatus.BAD_REQUEST);
        }

        // Validate new password strength
        validatePasswordStrength(request.getNewPassword());

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password reset successfully for user: {}", user.getEmail());

        return SuccessMessage.PASSWORD_RESET_SUCCESS;
    }


    @Transactional
    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        log.info("Forgot password request for email: {}", request.getEmail());

        if (!userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration attempt with existing email: {}", request.getEmail());
            return SuccessMessage.PASSWORD_RESET_LINK_SENT;
        }

        // Future: Generate reset token and send email
        // For now: Just return success message (security best practice)

        log.info("Forgot password request processed for email: {}", request.getEmail());

        return SuccessMessage.PASSWORD_RESET_LINK_SENT;
    }


    private void validatePasswordStrength(String password) {
        if (password.length() < 8) {
            throw new MoneyMapException(
                    "Password must be at least 8 characters",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new MoneyMapException(
                    "Password must contain at least one uppercase letter",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!password.matches(".*[a-z].*")) {
            throw new MoneyMapException(
                    "Password must contain at least one lowercase letter",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!password.matches(".*[0-9].*")) {
            throw new MoneyMapException(
                    "Password must contain at least one digit",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};:'\",.<>?/].*")) {
            throw new MoneyMapException(
                    "Password must contain at least one special character (!@#$%^&*)",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}