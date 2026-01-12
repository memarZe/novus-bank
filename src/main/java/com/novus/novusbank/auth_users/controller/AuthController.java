package com.novus.novusbank.auth_users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novus.novusbank.auth_users.dto.LoginRequest;
import com.novus.novusbank.auth_users.dto.LoginResponse;
import com.novus.novusbank.auth_users.dto.RegistrationRequest;
import com.novus.novusbank.auth_users.dto.ResetPasswordRequest;
import com.novus.novusbank.auth_users.services.AuthService;
import com.novus.novusbank.response.Response;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * AuthController handles authentication and user account related endpoints.
 * <p>
 * Endpoints:
 * <ul>
 * <li><b>POST /api/auth/register</b>: Register a new user account</li>
 * <li><b>POST /api/auth/login</b>: Authenticate a user and return a JWT
 * token</li>
 * <li><b>POST /api/auth/forgot-password</b>: Send a password reset link to the
 * user's email</li>
 * <li><b>POST /api/auth/reset-password</b>: Reset the user's password using a
 * reset code</li>
 * </ul>
 * <p>
 * This controller delegates business logic to the AuthService and returns
 * standardized responses.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<String>> register(@RequestBody @Valid RegistrationRequest request) {
        return ResponseEntity.ok(authService.register(request));

    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Response<?>> forgotPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(authService.forgetPassword(resetPasswordRequest.getEmail()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response<?>> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(authService.updatePasswordViaResetCode(resetPasswordRequest));
    }

}
