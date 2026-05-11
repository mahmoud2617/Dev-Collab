package com.mahmoud.devCollab.dto;

import com.mahmoud.devCollab.dto.customeValidation.ValidEmail;
import com.mahmoud.devCollab.dto.customeValidation.ValidPassword;
import com.mahmoud.devCollab.dto.customeValidation.ValidUsername;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterDto {
    @ValidUsername
    @Pattern(
        regexp = "^[a-zA-Z][a-zA-Z0-9_]{3,149}$",
        message = "Username must be in range 4-150 character, start with a letter, contain only letters, numbers, and underscore."
    )
    private String username;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
}
