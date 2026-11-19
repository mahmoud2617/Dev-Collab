package com.mahmoud.devCollab.service.project;

import com.mahmoud.devCollab.domain.entity.Project;
import com.mahmoud.devCollab.domain.entity.ProjectJoinRequest;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.enums.MemberRole;
import com.mahmoud.devCollab.domain.relation.ProjectMember;
import com.mahmoud.devCollab.dto.*;
import com.mahmoud.devCollab.exception.ConflictException;
import com.mahmoud.devCollab.exception.InvalidRequestDataException;
import com.mahmoud.devCollab.exception.NotFoundException;
import com.mahmoud.devCollab.mapper.ProjectJoinRequestMapper;
import com.mahmoud.devCollab.mapper.ProjectMapper;
import com.mahmoud.devCollab.mapper.UserMapper;
import com.mahmoud.devCollab.repository.ProjectJoinRequestRepository;
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
    private final ProjectUtilService projectUtilService;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectJoinRequestRepository projectJoinRequestRepository;
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;
    private final ProjectJoinRequestMapper projectJoinRequestMapper;

    public Page<ProjectDto> getAllProjects(Pageable pageable) {
        int page = Math.max(pageable.getPageNumber(), 0);
        int size = Math.min(
            pageable.getPageSize() == 0? 10 : pageable.getPageSize(),
            20
        );

        Sort sort = pageable.getSort().isSorted()?
                pageable.getSort()
                : Sort.by("id").ascending();

        List<String> allowedSortProperties = List.of("id", "name", "createdAt");

        for (Sort.Order order : sort) {
            if (!allowedSortProperties.contains(order.getProperty())) {
                throw new InvalidRequestDataException("Unsupported sort property.");
            }
        }

        pageable = PageRequest.of(page, size, sort);

        Page<Project> projects = projectRepository.findAll(pageable);

        return projects.map(projectMapper::toDto);
    }

    public ProjectDto getProjectDetails(Long projectId) {
        List<MemberRole> allowedMemberRoles = List.of(
            MemberRole.ADMIN,
            MemberRole.MEMBER
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            projectId,
            allowedMemberRoles
        );

        Project project = projectRepository.findByIdWithProjectMembers(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        return projectMapper.toDto(project);
    }

    @Transactional
    public CreateProjectResponse createProject(CreateProjectRequest request) {
        String name = request.getName().trim(),
               description = request.getDescription().trim();

        description = description.isEmpty() ? null : description;

        User user = currentUserService.getCurrentUser();

        LocalDateTime now = LocalDateTime.now();

        Project project = Project.builder()
                .name(name)
                .description(description)
                .owner(user)
                .createdAt(now)
                .updatedAt(now)
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

    public List<ProjectJoinRequestDto> getJoinRequests(Long projectId) {
        List<MemberRole> allowedMemberRoles = List.of(
            MemberRole.ADMIN
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            projectId,
            allowedMemberRoles
        );
        
        List<ProjectJoinRequest> joinRequests = projectJoinRequestRepository.findAllByProjectId(projectId);

        return joinRequests.stream()
                .map(projectJoinRequestMapper::toDto)
                .toList();
    }

    public void sendJoinRequest(Long projectId) {
        User user = currentUserService.getCurrentUser();

        projectUtilService.checkIfTheUserExistsInTheProject(projectId, user.getId());

        if (projectJoinRequestRepository.existsByProjectIdAndUserId(projectId, user.getId())) {
            throw new ConflictException("User already sent join request.");
        }

        var projectAndUser = projectUtilService.getProjectWithProjectMembersAndUserWithMemberships(projectId, user.getId());

        ProjectJoinRequest projectJoinRequest = ProjectJoinRequest.builder()
                .project(projectAndUser.project())
                .user(projectAndUser.user())
                .sentAt(LocalDateTime.now())
                .build();

        projectJoinRequestRepository.save(projectJoinRequest);
    }

    @Transactional
    public void acceptJoinRequest(Long projectId, Long userId) {
        List<MemberRole> allowedMemberRoles = List.of(
                MemberRole.ADMIN
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            projectId,
            allowedMemberRoles
        );

        ProjectJoinRequest joinRequest = projectJoinRequestRepository.findByProjectIdAndUserId(projectId, userId);

        if (joinRequest == null) {
            throw new NotFoundException("Join request not found.");
        }

        ProjectAndMember projectAndUser = projectUtilService.addUserToProject(projectId, userId);

        projectAndUser.project()
                .getProjectJoinRequests().remove(joinRequest);
    }

    @Transactional
    public void deleteJoinRequest(Long projectId, Long userId) {
        List<MemberRole> allowedMemberRoles = List.of(
                MemberRole.ADMIN
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            projectId,
            allowedMemberRoles
        );

         ProjectJoinRequest projectJoinRequest = projectJoinRequestRepository
                 .findByProjectIdAndUserId(projectId, userId);

         if (projectJoinRequest == null) {
             throw new NotFoundException("Join request not found.");
         }

         projectJoinRequestRepository.delete(projectJoinRequest);
    }

    public void changeProjectName(Long projectId, ChangeProjectNameRequest request) {
        changeProjectData(
            projectId,
            new ChangeProjectData(request.getName(), null)
        );
    }

    public void changeProjectDescription(Long projectId, ChangeProjectDescriptionRequest request) {
        changeProjectData(
            projectId,
            new ChangeProjectData(null, request.getDescription())
        );
    }

    public void deleteProject(Long projectId) {
        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
            currentUserService.getCurrentUserWithMemberships(),
            projectId,
            List.of()
        );

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        projectRepository.delete(project);
    }

    private void changeProjectData(Long projectId, ChangeProjectData data) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        String name = data.name() == null?
                project.getName()
                : data.name();
        String description = data.description() == null?
                project.getDescription()
                : data.description();

        List<MemberRole> allowedMemberRoles = List.of(
                MemberRole.ADMIN
        );

        projectAuthorizationService.isAuthorizedToDoAnActionOnTheProjectBasedOnRole(
                currentUserService.getCurrentUserWithMemberships(),
                projectId,
                allowedMemberRoles
        );

        project.setName(name);
        project.setDescription(description);
        project.setUpdatedAt(LocalDateTime.now());

        projectRepository.save(project);
    }
}
