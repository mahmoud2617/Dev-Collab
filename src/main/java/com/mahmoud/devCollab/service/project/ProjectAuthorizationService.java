package com.mahmoud.devCollab.service.project;

import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.enums.MemberRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProjectAuthorizationService {
    public void isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
        User currentUser,
        Long projectId,
        List<MemberRole> memberRoles
    ) {
        boolean authorized = currentUser.getMemberships().stream()
                    .anyMatch(projectMember ->
                        memberRoles.contains(projectMember.getRole()) &&
                        Objects.equals(projectMember.getProject().getId(), projectId)
                    );

        if (!authorized) {
            throw new AccessDeniedException("Insufficient permissions.");
        }
    }
}
