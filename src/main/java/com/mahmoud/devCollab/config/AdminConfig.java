package com.mahmoud.devCollab.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.admin")
@Getter
@Setter
public class AdminConfig {
    private String username;
    private String email;
    private String password;
}
