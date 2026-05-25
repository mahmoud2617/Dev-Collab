package com.mahmoud.devCollab.dto;

import com.mahmoud.devCollab.dto.customeValidation.ValidEmail;
import com.mahmoud.devCollab.dto.customeValidation.ValidPassword;
import com.mahmoud.devCollab.dto.customeValidation.ValidUsername;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterDto {
    @ValidUsername
    private String username;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
}
