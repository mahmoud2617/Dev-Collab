package com.mahmoud.devCollab.service.email;

import com.mahmoud.devCollab.config.VerificationConfig;
import com.mahmoud.devCollab.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationEmailService {
    private final EmailService emailService;
    private final VerificationConfig verificationConfig;

    @Value("${app.websiteUrl}")
    private String websiteUrl;

    public void sendVerificationEmail(User user, String token) {
        String verificationLink = websiteUrl + "/verify?token=" + token;

        String subject = "DevCollab Email Verification";

        String body = """
        Hello, %s

        Thank you for creating an account!
        
        To complete your registration, Please click the link below to verify your email address and activate your account:
        %s

        This verification link will expire in %d minutes.

        If you did not create an account, you can safely ignore this email.

        Best regards,
        The DevCollab Team
        """.formatted(
            user.getUsername(),
            verificationLink,
            verificationConfig.getEmailVerificationTokenExpiration() / 60
        );

        emailService.SendEmail(user.getEmail(), subject, body);
    }
}
