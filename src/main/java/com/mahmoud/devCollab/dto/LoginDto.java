package com.mahmoud.devCollab.dto;

import com.mahmoud.devCollab.dto.customeValidation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {
    @NotBlank(message = "Username or Email is required.")
    @Size(max = 250, message = "Property must not be more than 250 character.")
    private String usernameOrEmail;

    @ValidPassword
    private String password;
}
