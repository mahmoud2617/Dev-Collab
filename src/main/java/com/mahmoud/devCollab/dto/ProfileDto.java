package com.mahmoud.devCollab.dto;

import com.mahmoud.devCollab.domain.entity.ProfilePicture;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileDto {
    private UserDto user;
    private String bio;
    private ProfilePicture profilePicture;
    private List<SkillDto> skills;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
