package com.mahmoud.devCollab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeMemberRoleInProjectRequest {
    @NotBlank(message = "Membership role is required.")
    private String membershipRole;
}
