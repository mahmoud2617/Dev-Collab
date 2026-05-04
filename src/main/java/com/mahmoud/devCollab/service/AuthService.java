package com.mahmoud.devCollab.service;

import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.dto.LoginDto;
import com.mahmoud.devCollab.dto.RegisterDto;
import com.mahmoud.devCollab.exception.BadCredentialsException;
import com.mahmoud.devCollab.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterDto registerDto) {
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(encodedPassword)
                .build();

        userRepository.save(user);
    }

    public void login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsernameOrEmail()).orElse(null);

        if (user == null) {
            user = userRepository.findByEmail(loginDto.getUsernameOrEmail())
                    .orElseThrow(() -> new BadCredentialsException("Invalid username or email."));
        }

        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password.");
        }
    }
}
