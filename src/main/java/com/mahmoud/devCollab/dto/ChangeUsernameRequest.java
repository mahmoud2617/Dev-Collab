package com.mahmoud.devCollab.dto;

import com.mahmoud.devCollab.dto.customeValidation.ValidUsername;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChangeUsernameRequest {
    @ValidUsername
    private String newUsername;
}
