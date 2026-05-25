package com.mahmoud.devCollab.service.email;

import com.mahmoud.devCollab.config.VerificationConfig;
import com.mahmoud.devCollab.domain.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordResetVerificationEmailService {
    private final EmailService emailService;
    private final VerificationConfig verificationConfig;

    public void sendOtpEmail(User user, String code) {
        String subject = code + " is your DevCollab verification code";

        String body = """
        Hello, %s
        We received a request to reset your DevCollab account password.

        Your single-use verification code is:
        %s

        This code is confidential and will expire in %d minutes.

        If you did not initiate this request, someone may have typed your email address by mistake. You can safely ignore this message—your account remains secure.

        Regards,
        DevCollab Team
        """.formatted(
            user.getUsername(),
            code,
            verificationConfig.getOtpVerificationTokenExpiration() / 60
        );

        emailService.sendEmail(user.getEmail(), subject, body);
    }
}
