package com.mahmoud.devCollab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateProjectRequest {
    @NotBlank(message = "Project name is required.")
    private String name;
    private String description;
}
