package com.mahmoud.devCollab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto owner;
    private Integer numOfMembers;
    private Set<ProjectMemberDto> members;
}
