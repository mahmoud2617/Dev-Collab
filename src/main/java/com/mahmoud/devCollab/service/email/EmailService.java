package com.mahmoud.devCollab.service.email;

public interface EmailService {
    void SendEmail(String to, String subject, String body);
}
