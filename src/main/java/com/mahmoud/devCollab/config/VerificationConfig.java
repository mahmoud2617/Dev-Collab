package com.mahmoud.devCollab.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.verification")
@Getter
@Setter
public class VerificationConfig {
    private int emailVerificationTokenExpiration;
    private int otpVerificationTokenExpiration;
    private int resetPasswordSessionVerificationTokenExpiration;
}
