package com.mahmoud.devCollab.service;

import com.mahmoud.devCollab.config.JwtConfig;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.enums.Role;
import com.mahmoud.devCollab.dto.JwtResponse;
import com.mahmoud.devCollab.dto.LoginDto;
import com.mahmoud.devCollab.dto.RegisterDto;
import com.mahmoud.devCollab.exception.UnauthorizedUserException;
import com.mahmoud.devCollab.repository.UserRepository;
import com.mahmoud.devCollab.security.CustomUserDetails;
import com.mahmoud.devCollab.security.jwt.Jwt;
import com.mahmoud.devCollab.security.jwt.JwtService;
import com.mahmoud.devCollab.security.jwt.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
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
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
            )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Token token = new Token(
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            userDetails.getRole()
        );

        Jwt accessToken = jwtService.generateAccessToken(token);
        Jwt refreshToken = jwtService.generateRefreshToken(token);

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

        Jwt accessToken = jwtService.generateAccessToken(
            new Token(user.getId(), user.getUsername(), user.getEmail(), user.getRole())
        );

        return new JwtResponse(accessToken.toString());
    }
}
