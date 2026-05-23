package com.mahmoud.devCollab.listener;

import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.event.UserRegisteredEvent;
import com.mahmoud.devCollab.service.email.EmailVerificationEmailService;
import com.mahmoud.devCollab.service.token.EmailVerificationService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserRegisteredListener {
    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationEmailService emailVerificationEmailService;

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        User user = event.getUser();

        String token = emailVerificationService.createToken(user);

        emailVerificationEmailService.sendVerificationEmail(user,token);
    }
}
