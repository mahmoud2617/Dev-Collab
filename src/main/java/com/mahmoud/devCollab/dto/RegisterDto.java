package com.mahmoud.devCollab.dto;

import com.mahmoud.devCollab.dto.customeValidation.ValidEmail;
import com.mahmoud.devCollab.dto.customeValidation.ValidPassword;
import com.mahmoud.devCollab.dto.customeValidation.ValidUsername;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDto {
    @ValidUsername
    @Pattern(
        regexp = "^[a-zA-Z][a-zA-Z0-9_]{2,148}[a-zA-Z]$",
        message = "Username must be in range 4-150 character, start and end with a letter, contain only letters, numbers, and underscore."
    )
    private String username;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
}
