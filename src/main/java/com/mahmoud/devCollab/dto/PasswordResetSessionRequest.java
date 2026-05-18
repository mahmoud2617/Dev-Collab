package com.mahmoud.devCollab.dto;

import com.mahmoud.devCollab.dto.customeValidation.ValidEmail;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordResetSessionRequest {
    @ValidEmail
    private String email;

    @NotNull(message = "Code is required.")
    @Pattern(regexp = "^\\d{6}$", message = "Code must be exactly 6 digits.")
    private Integer code;
}
