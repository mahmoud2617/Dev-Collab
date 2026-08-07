package com.mahmoud.devCollab.dto;

import com.mahmoud.devCollab.domain.entity.ProfilePicture;
import com.mahmoud.devCollab.domain.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
public class ProfileDto {
    private UserDto user;
    private String bio;
    private ProfilePicture profilePicture;
    private Set<Skill> skills;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
