package com.mahmoud.devCollab.mapper;

import com.mahmoud.devCollab.domain.entity.Skill;
import com.mahmoud.devCollab.dto.SkillDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    SkillDto toDto(Skill skill);
}
