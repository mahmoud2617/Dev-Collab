package com.mahmoud.devCollab.admin;

import com.mahmoud.devCollab.config.AdminConfig;
import com.mahmoud.devCollab.domain.entity.Profile;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.enums.Role;
import com.mahmoud.devCollab.event.AdminInitializedEvent;
import com.mahmoud.devCollab.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final AdminConfig adminConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void run(String... args) {
        if (adminConfig.getUsername().isBlank()
            || adminConfig.getEmail().isBlank()
            || adminConfig.getPassword().isBlank())
            return;

        if (userRepository.existsByUsername(adminConfig.getUsername())
            || userRepository.existsByEmail(adminConfig.getEmail()))
            return;

        User admin = User.builder()
                .username(adminConfig.getUsername())
                .email(adminConfig.getEmail())
                .password(passwordEncoder.encode(adminConfig.getPassword()))
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        Profile profile = Profile.builder()
                .user(admin)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        admin.setProfile(profile);

        userRepository.save(admin);

        eventPublisher.publishEvent(new AdminInitializedEvent(admin));
    }
}
