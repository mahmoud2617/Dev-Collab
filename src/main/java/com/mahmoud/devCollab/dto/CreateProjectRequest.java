package com.mahmoud.devCollab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateProjectRequest {
    @NotBlank(message = "Project name is required.")
    private String name;

    @NotNull(message = "Description cannot be null.")
    private String description;
}
