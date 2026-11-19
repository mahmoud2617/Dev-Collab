package com.mahmoud.devCollab.service.project;

import com.mahmoud.devCollab.domain.entity.Project;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.enums.MemberRole;
import com.mahmoud.devCollab.domain.relation.ProjectMember;
import com.mahmoud.devCollab.exception.ConflictException;
import com.mahmoud.devCollab.exception.NotFoundException;
import com.mahmoud.devCollab.repository.ProjectMemberRepository;
import com.mahmoud.devCollab.repository.ProjectRepository;
import com.mahmoud.devCollab.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProjectUtilService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public ProjectAndMember addUserToProject(Long projectId, Long userId) {
        checkIfTheUserExistsInTheProject(projectId, userId);

        var projectAndUser = getProjectWithProjectMembersAndUserWithMemberships(projectId, userId);
        Project project = projectAndUser.project();
        User user = projectAndUser.user();

        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .member(user)
                .role(MemberRole.MEMBER)
                .build();

        projectMemberRepository.save(projectMember);

        project.getProjectMembers().add(projectMember);
        user.getMemberships().add(projectMember);

        return new ProjectAndMember(project, user);
    }

    public ProjectAndMember getProjectWithProjectMembersAndUserWithMemberships(Long projectId, Long userId) {
        Project project = projectRepository.findByIdWithProjectMembers(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        User member = userRepository.findByIdWithMemberships(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        return new ProjectAndMember(project, member);
    }

    public void checkIfTheUserExistsInTheProject(Long projectId, Long memberId) {
        if (projectMemberRepository.existsByProjectIdAndMemberId(projectId, memberId)) {
            throw new ConflictException("User is already a member of this project.");
        }
    }

    public void checkIfTheUserIsNotExistInTheProject(Long projectId, Long memberId) {
        if (!projectMemberRepository.existsByProjectIdAndMemberId(projectId, memberId)) {
            throw new NotFoundException("User is not a member of this project.");
        }
    }
}

