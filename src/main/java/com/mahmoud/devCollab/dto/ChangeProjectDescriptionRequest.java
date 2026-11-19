package com.mahmoud.devCollab.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeProjectDescriptionRequest {
    @NotNull(message = "Description cannot be null.")
    private String description;
}
