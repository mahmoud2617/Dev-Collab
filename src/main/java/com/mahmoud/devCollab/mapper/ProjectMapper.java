package com.mahmoud.devCollab.mapper;

import com.mahmoud.devCollab.domain.entity.Project;
import com.mahmoud.devCollab.dto.ProjectDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProjectMemberMapper.class})
public interface ProjectMapper {
    @Mapping(target = "members", source = "projectMembers")
    @Mapping(
        target = "numOfMembers",
        expression = "java(project.getProjectMembers() != null ? project.getProjectMembers().size() : 0)"
    )
    ProjectDto toDto(Project project);
}
