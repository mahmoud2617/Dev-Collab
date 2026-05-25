package com.mahmoud.devCollab.listener;

import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.event.AdminInitializedEvent;
import com.mahmoud.devCollab.service.email.AdminEmailService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdminInitializedListener {
    private final AdminEmailService adminEmailService;

    @EventListener
    public void handleAdminInitializedEvent(AdminInitializedEvent event) {
        User admin = event.getAdmin();

        adminEmailService.sendAdminInitializedEmail(admin);
    }
}
