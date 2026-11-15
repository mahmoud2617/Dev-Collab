package com.mahmoud.devCollab.service.project;

import com.mahmoud.devCollab.domain.entity.Project;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.enums.MemberRole;
import com.mahmoud.devCollab.domain.relation.ProjectMember;
import com.mahmoud.devCollab.dto.*;
import com.mahmoud.devCollab.exception.NotFoundException;
import com.mahmoud.devCollab.mapper.ProjectMapper;
import com.mahmoud.devCollab.mapper.UserMapper;
import com.mahmoud.devCollab.repository.ProjectMemberRepository;
import com.mahmoud.devCollab.repository.ProjectRepository;
import com.mahmoud.devCollab.service.user.CurrentUserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {
    private final CurrentUserService currentUserService;
    private final ProjectAuthorizationService projectAuthorizationService;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;

    public Page<ProjectDto> getAllProjects(Pageable pageable) {
        int size = Math.min(pageable.getPageSize(), 20);

        Sort.Order sort = pageable.getSort()
                .stream()
                .findFirst()
                .orElse(Sort.Order.by("id"));

        String property = sort.getProperty();
        Sort.Direction direction = sort.getDirection();

        pageable = PageRequest.of(pageable.getPageNumber(), size, direction, property);

        Page<Project> projects = projectRepository.findAll(pageable);

        return projects.map(projectMapper::toDto);
    }

    public ProjectDto getProjectDetails(Long id) {
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

        Project project = projectRepository.findByIdWithProjectMembers(id)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        return projectMapper.toDto(project);
    }

    @Transactional
    public CreateProjectResponse createProject(CreateProjectRequest request) {
        String name = request.getName().trim(),
               description = request.getDescription().trim();

        description = description.isEmpty() ? null : description;

        User user = currentUserService.getCurrentUser();

        Project project = Project.builder()
                .name(name)
                .description(description)
                .owner(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        projectRepository.save(project);

        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .member(user)
                .role(MemberRole.OWNER)
                .build();

        projectMemberRepository.save(projectMember);

        return new CreateProjectResponse(
            project.getId(),
            name,
            description,
            userMapper.toDto(user),
            project.getCreatedAt()
        );
    }

    public void changeProjectName(Long id, ChangeProjectNameRequest request) {
        List<MemberRole> allowedMemberRoles = List.of(
            MemberRole.OWNER,
            MemberRole.ADMIN
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            id,
            allowedMemberRoles
        );

        String name = request.getName().trim();

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        project.setName(name);
        project.setUpdatedAt(LocalDateTime.now());

        projectRepository.save(project);
    }

    public void changeProjectDescription(Long id, ChangeProjectDescriptionRequest request) {
        List<MemberRole> allowedMemberRoles = List.of(
            MemberRole.OWNER,
            MemberRole.ADMIN
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            id,
            allowedMemberRoles
        );

        String description = request.getDescription().trim();
        description = description.isEmpty() ? null : description;

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        project.setDescription(description);
        project.setUpdatedAt(LocalDateTime.now());

        projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        List<MemberRole> allowedMemberRoles = List.of(
            MemberRole.OWNER
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            id,
            allowedMemberRoles
        );

        projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        projectRepository.deleteById(id);
    }
}
