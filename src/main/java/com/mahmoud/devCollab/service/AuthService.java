package com.mahmoud.devCollab.service;

import com.mahmoud.devCollab.config.JwtConfig;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.enums.Role;
import com.mahmoud.devCollab.dto.JwtResponse;
import com.mahmoud.devCollab.dto.LoginDto;
import com.mahmoud.devCollab.dto.RegisterDto;
import com.mahmoud.devCollab.exception.BadCredentialsException;
import com.mahmoud.devCollab.exception.UnauthorizedUserException;
import com.mahmoud.devCollab.repository.UserRepository;
import com.mahmoud.devCollab.security.jwt.Jwt;
import com.mahmoud.devCollab.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;

    public void register(RegisterDto registerDto) {
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public JwtResponse login(LoginDto loginDto, HttpServletResponse response) {
        User user = userRepository.findByUsername(loginDto.getUsernameOrEmail()).orElse(null);

        if (user == null) {
            user = userRepository.findByEmail(loginDto.getUsernameOrEmail())
                    .orElseThrow(() -> new BadCredentialsException("Invalid username or email."));
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password.");
        }

        Jwt accessToken = jwtService.generateAccessToken(user);
        Jwt refreshToken = jwtService.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        response.addCookie(cookie);

        return new JwtResponse(accessToken.toString());
    }

    public JwtResponse refreshLogin(String refreshToken) {
        Jwt jwt = jwtService.parseToken(refreshToken);

        if (jwt == null || jwt.isExpired()) {
            throw new UnauthorizedUserException();
        }

        User user = userRepository.findByUsername(jwt.getUsername())
                .orElseThrow(UnauthorizedUserException::new);

        Jwt accessToken = jwtService.generateAccessToken(user);

        return new JwtResponse(accessToken.toString());
    }
}
