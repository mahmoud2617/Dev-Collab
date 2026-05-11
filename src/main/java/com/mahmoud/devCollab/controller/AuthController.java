package com.mahmoud.devCollab.controller;

import com.mahmoud.devCollab.dto.JwtResponse;
import com.mahmoud.devCollab.dto.LoginDto;
import com.mahmoud.devCollab.dto.RegisterDto;
import com.mahmoud.devCollab.service.AuthService;
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
}
