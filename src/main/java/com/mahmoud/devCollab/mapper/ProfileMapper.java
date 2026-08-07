package com.mahmoud.devCollab.mapper;

import com.mahmoud.devCollab.domain.entity.Profile;
import com.mahmoud.devCollab.dto.ProfileDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDto toDto(Profile profile);
}
