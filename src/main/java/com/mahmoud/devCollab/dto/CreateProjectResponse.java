package com.mahmoud.devCollab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateProjectResponse {
    private Long id;
    private String name;
    private String description;
    private UserDto owner;
    private LocalDateTime createdAt;
}
