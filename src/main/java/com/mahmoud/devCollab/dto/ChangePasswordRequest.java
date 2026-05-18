package com.mahmoud.devCollab.dto;

import com.mahmoud.devCollab.dto.customeValidation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangePasswordRequest {
    @ValidPassword
    private String currentPassword;

    @ValidPassword
    private String newPassword;
}
