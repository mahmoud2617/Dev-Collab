package com.mahmoud.devCollab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditSkillRequest {
    @NotBlank(message = "Skill new name is required.")
    private String newName;
}
