package com.mahmoud.devCollab.controller;

import com.mahmoud.devCollab.dto.LoginDto;
import com.mahmoud.devCollab.dto.RegisterDto;
import com.mahmoud.devCollab.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public void register(
        @Valid @RequestBody RegisterDto registerDto
    ) {
        authService.register(registerDto);
    }

    @PostMapping("/login")
    public void login(
        @Valid @RequestBody LoginDto loginDto
    ) {
        authService.login(loginDto);
    }
}
