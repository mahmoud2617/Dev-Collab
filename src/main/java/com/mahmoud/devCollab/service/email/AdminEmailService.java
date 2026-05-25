package com.mahmoud.devCollab.service.email;

import com.mahmoud.devCollab.domain.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminEmailService {
    private final EmailService emailService;

    public void sendAdminInitializedEmail(User admin) {
        String subject = "DevCollab Admin Account Created";

        String body = """
        Hello, %s

        Your admin account has been successfully initialized in DevCollab.

        Username: %s
        Email: %s

        You can now access the system with your admin privileges.

        Regards,
        DevCollab Team
        """.formatted(
            admin.getUsername(),
            admin.getUsername(),
            admin.getEmail()
        );

        emailService.sendEmail(admin.getEmail(), subject, body);
    }
}
