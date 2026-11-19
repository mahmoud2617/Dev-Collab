package com.mahmoud.devCollab.service.project;

import com.mahmoud.devCollab.domain.entity.Project;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.enums.MemberRole;
import com.mahmoud.devCollab.domain.relation.ProjectMember;
import com.mahmoud.devCollab.dto.ChangeMemberRoleInProjectRequest;
import com.mahmoud.devCollab.exception.InvalidRequestDataException;
import com.mahmoud.devCollab.exception.NotFoundException;
import com.mahmoud.devCollab.service.user.CurrentUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProjectMemberService {
    private final CurrentUserService currentUserService;
    private final ProjectAuthorizationService projectAuthorizationService;
    private final ProjectUtilService projectUtilService;

    @Transactional
    public void addUserToProject(Long projectId, Long userId) {
        projectUtilService.addUserToProject(projectId, userId);
    }

    @Transactional
    public void changeMemberRoleInTheProject(Long projectId, Long memberId, ChangeMemberRoleInProjectRequest request) {
        List<MemberRole> allowedMemberRoles = List.of(
            MemberRole.ADMIN
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            projectId,
            allowedMemberRoles
        );

        projectUtilService.checkIfTheUserIsNotExistInTheProject(projectId, memberId);

        var projectAndMember = projectUtilService.getProjectWithProjectMembersAndUserWithMemberships(projectId, memberId);
        User member = projectAndMember.user();

        MemberRole memberRole;

        try {
            memberRole = MemberRole.valueOf(request.getMembershipRole().trim());
        } catch (Exception e) {
            throw new InvalidRequestDataException("Invalid membership role.");
        }

        if (memberRole.equals(MemberRole.OWNER)) {
            throw new InvalidRequestDataException("Cannot change member role to owner.");
        }

        ProjectMember projectMember = member.getMemberships().stream()
                .filter(
                    pm ->
                    Objects.equals(pm.getProject().getId(), projectId)
                ).findFirst()
                .orElseThrow(() -> new NotFoundException("Project not found."));


        if (projectMember.getRole().equals(MemberRole.OWNER)) {
            throw new InvalidRequestDataException("Cannot change the owner's role.");
        }

        projectMember.setRole(memberRole);
    }

    @Transactional
    public void removeMemberFromProject(Long projectId, Long memberId) {
        List<MemberRole> allowedMemberRoles = List.of(
            MemberRole.ADMIN
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            projectId,
            allowedMemberRoles
        );

        projectUtilService.checkIfTheUserIsNotExistInTheProject(projectId, memberId);

        var projectAndMember = projectUtilService.getProjectWithProjectMembersAndUserWithMemberships(projectId, memberId);
        Project project = projectAndMember.project();
        User member = projectAndMember.user();
        
        ProjectMember projectMember = member.getMemberships().stream()
                .filter(pm ->
                    Objects.equals(pm.getProject().getId(), projectId)
                ).findFirst()
                .orElseThrow(() -> new NotFoundException("User is not a member of this project."));

        if (projectMember.getRole().equals(MemberRole.OWNER)) {
            throw new InvalidRequestDataException("Cannot remove the project owner.");
        }

        project.getProjectMembers().remove(projectMember);
        member.getMemberships().remove(projectMember);
    }
}
