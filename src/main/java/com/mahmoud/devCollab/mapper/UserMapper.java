package com.mahmoud.devCollab.mapper;

import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
