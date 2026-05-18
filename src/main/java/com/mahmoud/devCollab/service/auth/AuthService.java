package com.mahmoud.devCollab.service.auth;

import com.mahmoud.devCollab.config.JwtConfig;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.enums.Role;
import com.mahmoud.devCollab.dto.*;
import com.mahmoud.devCollab.exception.EmailNotVerifiedException;
import com.mahmoud.devCollab.exception.InvalidRequestDataException;
import com.mahmoud.devCollab.exception.UnauthorizedUserException;
import com.mahmoud.devCollab.repository.UserRepository;
import com.mahmoud.devCollab.security.CustomUserDetails;
import com.mahmoud.devCollab.security.jwt.Jwt;
import com.mahmoud.devCollab.security.jwt.JwtService;
import com.mahmoud.devCollab.security.jwt.Token;
import com.mahmoud.devCollab.service.email.EmailVerificationEmailService;
import com.mahmoud.devCollab.service.email.PasswordResetVerificationEmailService;
import com.mahmoud.devCollab.service.token.EmailVerificationService;
import com.mahmoud.devCollab.service.token.PasswordResetSessionVerificationService;
import com.mahmoud.devCollab.service.token.PasswordResetVerificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailVerificationEmailService emailVerificationEmailService;
    private final PasswordResetVerificationEmailService passwordResetVerificationEmailService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetVerificationService passwordResetVerificationService;
    private final PasswordResetSessionVerificationService passwordResetSessionVerificationService;
    private final JwtConfig jwtConfig;

    public User getCurrentUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            throw new UnauthorizedUserException();
        }

        return userRepository.findById(customUserDetails.getId()).orElseThrow(UnauthorizedUserException::new);
    }

    @Transactional
    public void register(RegisterDto registerDto) {
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(encodedPassword)
                .role(Role.USER)
                .enabled(false)
                .build();

        userRepository.save(user);

        String token = emailVerificationService.createToken(user);

        emailVerificationEmailService.sendVerificationEmail(user,token);
    }

    public JwtResponse login(LoginDto loginDto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
            )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (!userDetails.getEnabled()) {
            throw new EmailNotVerifiedException();
        }

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

    @Transactional
    public void verifyEmail(String token) {
        User user = emailVerificationService.validateToken(token);
        user.setEnabled(true);

        emailVerificationService.deleteTokens(user);
    }

    public void resendEmailVerification(ResendEmailVerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user != null) {
            emailVerificationService.deleteTokens(user);

            String token = emailVerificationService.createToken(user);

            emailVerificationEmailService.sendVerificationEmail(user, token);
        }
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = userRepository.findById(getCurrentUser().getId())
                .orElseThrow(UnauthorizedUserException::new);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidRequestDataException("Current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    public void forgetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user != null) {
            passwordResetVerificationService.deleteTokens(user);

            String code = passwordResetVerificationService.createToken(user);

            passwordResetVerificationEmailService.sendOtpEmail(user, code);
        }
    }

    @Transactional
    public PasswordResetSessionResponse verifyPasswordResetOtp(PasswordResetSessionRequest request) {
        User user = passwordResetVerificationService.validateToken(String.valueOf(request.getCode()));

        passwordResetVerificationService.deleteTokens(user);

        String resetToken = passwordResetSessionVerificationService.createToken(user);

        return new PasswordResetSessionResponse(resetToken);
    }

    @Transactional
    public void resetForgottenPassword(ResetForgottenPasswordAfterVerificationRequest request) {
        User user = passwordResetSessionVerificationService.validateToken(request.getResetToken());

        if (Objects.equals(user.getEmail(), request.getEmail())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));

            userRepository.save(user);

            passwordResetSessionVerificationService.deleteTokens(user);
        }
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
