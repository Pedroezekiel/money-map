package com.projects.money_map_api.service;

import com.projects.money_map_api.dto.request.ForgotPasswordRequest;
import com.projects.money_map_api.dto.request.LoginRequest;
import com.projects.money_map_api.dto.request.RegisterRequest;
import com.projects.money_map_api.dto.request.ResetPasswordRequest;
import com.projects.money_map_api.dto.response.AuthResponse;
import com.projects.money_map_api.entity.User;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    String resetPassword(User user, ResetPasswordRequest request);

    String forgotPassword(ForgotPasswordRequest request);
}
