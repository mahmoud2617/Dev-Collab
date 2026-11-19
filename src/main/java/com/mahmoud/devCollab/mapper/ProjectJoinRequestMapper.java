package com.mahmoud.devCollab.mapper;

import com.mahmoud.devCollab.domain.entity.ProjectJoinRequest;
import com.mahmoud.devCollab.dto.ProjectJoinRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectJoinRequestMapper {
    ProjectJoinRequestDto toDto(ProjectJoinRequest projectJoinRequest);
}
