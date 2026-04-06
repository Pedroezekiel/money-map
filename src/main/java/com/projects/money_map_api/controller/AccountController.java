package com.projects.money_map_api.controller;

import com.projects.money_map_api.dto.request.AccountRequest;
import com.projects.money_map_api.dto.response.AccountResponse;
import com.projects.money_map_api.dto.response.BaseResponse;
import com.projects.money_map_api.entity.Account;
import com.projects.money_map_api.entity.User;
import com.projects.money_map_api.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountException;

@RestController
@RequestMapping("/api/v1/accounts")
@Slf4j
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping()
    @Operation(summary = "Create new account", description = "Create a new Money map account for the authenticated user")
    public ResponseEntity<?> createAccount(@AuthenticationPrincipal User user, @RequestBody AccountRequest request) throws AccountException {
        AccountResponse response = accountService.createAccount(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.ok(201, "Account created successfully", response));
    }
}
