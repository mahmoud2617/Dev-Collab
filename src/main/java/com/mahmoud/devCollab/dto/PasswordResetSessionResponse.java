package com.mahmoud.devCollab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordResetSessionResponse {
    private String resetToken;
}
