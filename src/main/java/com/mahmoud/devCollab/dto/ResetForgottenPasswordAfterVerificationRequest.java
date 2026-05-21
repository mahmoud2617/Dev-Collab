package com.mahmoud.devCollab.dto;

import com.mahmoud.devCollab.dto.customeValidation.ValidEmail;
import com.mahmoud.devCollab.dto.customeValidation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResetForgottenPasswordAfterVerificationRequest {
    @ValidEmail
    private String email;

    @NotBlank(message = "Reset Token is required")
    private String resetToken;

    @ValidPassword
    private String newPassword;
}
