package com.mahmoud.devCollab.service.project;

import com.mahmoud.devCollab.domain.entity.Project;
import com.mahmoud.devCollab.domain.entity.User;

public record ProjectAndMember(Project project, User user) {
}
