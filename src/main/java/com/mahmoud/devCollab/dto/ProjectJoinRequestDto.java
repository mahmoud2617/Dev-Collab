package com.mahmoud.devCollab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProjectJoinRequestDto {
    private Long id;
    private UserDto user;
    private LocalDateTime sentAt;
}
