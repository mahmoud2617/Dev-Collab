package com.mahmoud.devCollab.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeBioRequest {
    @Size(min = 1, max = 500, message = "Bio must be at most 500 characters long.")
    private String bio;
}
