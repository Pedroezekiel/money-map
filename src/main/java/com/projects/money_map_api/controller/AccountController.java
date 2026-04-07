package com.projects.money_map_api.controller;

import com.projects.money_map_api.dto.request.AccountRequest;
import com.projects.money_map_api.dto.response.AccountResponse;
import com.projects.money_map_api.dto.response.BaseResponse;
import com.projects.money_map_api.entity.User;
import com.projects.money_map_api.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping()
    @Operation(summary = "Edit account", description = "Edit an existing Money map account for the authenticated user")
    public ResponseEntity<?> editAccount(@AuthenticationPrincipal User user, @RequestParam String accountId, @RequestBody AccountRequest request) throws AccountException {
        AccountResponse response = accountService.editAccount(user, accountId, request);
        return ResponseEntity.ok(
                BaseResponse.ok(200, "Account edited successfully", response));
    }

    @GetMapping()
    @Operation(summary = "View account", description = "View details of a specific Money map account for the authenticated user")
    public ResponseEntity<?> viewAccount(@AuthenticationPrincipal User user, @RequestParam String accountId) throws AccountException {
        AccountResponse response = accountService.viewAccount(user, accountId);
        return ResponseEntity.ok(
                BaseResponse.ok(200, "Account retrieved successfully", response));
    }

    @GetMapping("/all")
    @Operation(summary = "View all accounts", description = "View all Money map accounts for the authenticated user with pagination")
    public ResponseEntity<?> viewAccounts(@AuthenticationPrincipal User user, @RequestParam int page, @RequestParam int size) {
        Page<AccountResponse> response = accountService.viewAccounts(user, page, size);
        return ResponseEntity.ok(
                BaseResponse.ok(200,
                        "Accounts retrieved successfully", response));
    }

    @DeleteMapping()
    @Operation(summary = "Delete account", description = "Delete a specific Money map account for the authenticated user")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal User user, @RequestParam String accountId) {
        String response = accountService.deleteAccount(user, accountId);
        return ResponseEntity.ok(
                BaseResponse.ok(200, "Account deleted successfully", response));
    }

}
