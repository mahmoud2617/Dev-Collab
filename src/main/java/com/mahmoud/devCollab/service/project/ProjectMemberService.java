package com.mahmoud.devCollab.service.project;

import com.mahmoud.devCollab.domain.entity.Project;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.enums.MemberRole;
import com.mahmoud.devCollab.domain.relation.ProjectMember;
import com.mahmoud.devCollab.dto.ChangeMemberRoleInProjectRequest;
import com.mahmoud.devCollab.exception.ConflictException;
import com.mahmoud.devCollab.exception.InvalidRequestDataException;
import com.mahmoud.devCollab.exception.NotFoundException;
import com.mahmoud.devCollab.repository.ProjectMemberRepository;
import com.mahmoud.devCollab.repository.ProjectRepository;
import com.mahmoud.devCollab.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public void addMember(Long projectId, Long id) {
        List<MemberRole> allowedMemberRoles = List.of(
            MemberRole.OWNER,
            MemberRole.ADMIN
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            id,
            allowedMemberRoles
        );

        Project project = projectRepository.findByIdWithProjectMembers(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        User member = userRepository.findByIdWithMemberships(id)
                .orElseThrow(() -> new NotFoundException("Member not found."));

        if (projectMemberRepository.existsByProjectIdAndMemberId(projectId, id)) {
            throw new ConflictException("Member already in the project.");
        }

        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .member(member)
                .role(MemberRole.MEMBER)
                .build();

        projectMemberRepository.save(projectMember);

        project.getProjectMembers().add(projectMember);
        member.getMemberships().add(projectMember);
    }

    @Transactional
    public void changeMemberRoleInTheProject(Long projectId, Long id, ChangeMemberRoleInProjectRequest request) {
        List<MemberRole> allowedMemberRoles = List.of(
            MemberRole.OWNER,
            MemberRole.ADMIN,
            MemberRole.MEMBER
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            id,
            allowedMemberRoles
        );

        projectRepository.findByIdWithProjectMembers(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        User member = userRepository.findByIdWithMemberships(id)
                .orElseThrow(() -> new NotFoundException("Member not found."));

        String membershipRole = request.getMembershipRole().trim();
        MemberRole memberRole;

        try {
            memberRole = MemberRole.valueOf(membershipRole);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestDataException("No membership role called \"" + membershipRole + "\"");
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

        projectMember.setRole(memberRole);
    }

    @Transactional
    public void removeMemberFromProject(Long projectId, Long id) {
        List<MemberRole> allowedMemberRoles = List.of(
            MemberRole.OWNER,
            MemberRole.ADMIN
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            id,
            allowedMemberRoles
        );

        Project project = projectRepository.findByIdWithProjectMembers(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        User member = userRepository.findByIdWithMemberships(id)
                .orElseThrow(() -> new NotFoundException("Member not found."));
        
        ProjectMember projectMember = member.getMemberships().stream()
                .filter(pm ->
                    Objects.equals(pm.getProject().getId(), projectId)
                ).findFirst()
                .orElseThrow(() -> new NotFoundException("User is not a member of this project."));

        project.getProjectMembers().remove(projectMember);
        member.getMemberships().remove(projectMember);
    }
}
