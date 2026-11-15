package com.mahmoud.devCollab.mapper;

import com.mahmoud.devCollab.domain.relation.ProjectMember;
import com.mahmoud.devCollab.dto.ProjectMemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {
    @Mapping(target = "membershipRole", source = "role")
    ProjectMemberDto toDto(ProjectMember projectMember);
}
