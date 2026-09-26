package com.mahmoud.devCollab.mapper;

import com.mahmoud.devCollab.domain.entity.Profile;
import com.mahmoud.devCollab.domain.relation.UserSkill;
import com.mahmoud.devCollab.dto.ProfileDto;
import com.mahmoud.devCollab.dto.SkillDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(target = "skills", source = "user.userSkills")
    ProfileDto toDto(Profile profile);

    @Mapping(target = "id", source = "skill.id")
    @Mapping(target = "name", source = "skill.name")
    SkillDto userSkillToSkillDto(UserSkill userSkill);
}
