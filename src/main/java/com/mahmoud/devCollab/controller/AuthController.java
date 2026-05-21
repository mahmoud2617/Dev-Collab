package com.mahmoud.devCollab.controller;

import com.mahmoud.devCollab.dto.*;
import com.mahmoud.devCollab.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
        @Valid @RequestBody RegisterDto registerDto
    ) {
        authService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
        @Valid @RequestBody LoginDto loginDto,
        HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.login(loginDto, response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
        @CookieValue(value = "refreshToken") String refreshToken
    ) {
        return ResponseEntity.ok(authService.refreshLogin(refreshToken));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(
        @RequestParam String token
    ) {
        authService.verifyEmail(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-email-verification")
    public ResponseEntity<Void> resendVerification(
        @Valid @RequestBody ResendEmailVerificationRequest request
    ) {
        authService.resendEmailVerification(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
        @Valid @RequestBody ChangePasswordRequest request
    ) {
        authService.changePassword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forget-password")
    public ResponseEntity<Void> forgetPassword(
        @Valid @RequestBody ForgetPasswordRequest request
    ) {
        authService.forgetPassword(request.getEmail());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forget-password/verify-otp")
    public ResponseEntity<PasswordResetSessionResponse> verifyPasswordResetOtp(
        @Valid @RequestBody PasswordResetSessionRequest request
    ) {
        PasswordResetSessionResponse response = authService.verifyPasswordResetOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forget-password/reset-password")
    public ResponseEntity<Void> resetForgottenPassword(
        @Valid @RequestBody ResetForgottenPasswordAfterVerificationRequest request
    ) {
        authService.resetForgottenPassword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.noContent().build();
    }
}
