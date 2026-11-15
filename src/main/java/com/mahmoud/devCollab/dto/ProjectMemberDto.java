package com.mahmoud.devCollab.dto;

import com.mahmoud.devCollab.domain.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectMemberDto {
    private UserDto member;
    private MemberRole membershipRole;
}
