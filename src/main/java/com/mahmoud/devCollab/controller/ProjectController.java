package com.mahmoud.devCollab.controller;

import com.mahmoud.devCollab.dto.*;
import com.mahmoud.devCollab.service.project.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public Page<ProjectDto> getAllProjects(
        Pageable pageable
    ) {
        return projectService.getAllProjects(pageable);
    }

    @GetMapping("/{projectId}")
    public ProjectDto getProject(
        @PathVariable Long projectId
    ) {
        return projectService.getProjectDetails(projectId);
    }

    @PostMapping
    public ResponseEntity<CreateProjectResponse> createProject(
        @Valid @RequestBody CreateProjectRequest request
    ) {
        return ResponseEntity.status(201).body(projectService.createProject(request));
    }

    @GetMapping("/{projectId}/join-requests")
    public ResponseEntity<List<ProjectJoinRequestDto>> getJoinRequests(
        @PathVariable Long projectId
    ) {
        return ResponseEntity.ok().body(projectService.getJoinRequests(projectId));
    }

    @PostMapping("/{projectId}/join-requests")
    public ResponseEntity<Void> sendJoinRequest(
        @PathVariable Long projectId
    ) {
        projectService.sendJoinRequest(projectId);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/{projectId}/join-requests/{userId}/accept")
    public ResponseEntity<Void> acceptJoinRequest(
        @PathVariable Long projectId,
        @PathVariable Long userId
    ) {
        projectService.acceptJoinRequest(projectId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{projectId}/join-requests/{userId}")
    public ResponseEntity<Void> deleteJoinRequest(
        @PathVariable Long projectId,
        @PathVariable Long userId
    ) {
        projectService.deleteJoinRequest(projectId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{projectId}/change-name")
    public ResponseEntity<Void> changeProjectName(
        @PathVariable Long projectId,
        @Valid @RequestBody ChangeProjectNameRequest request
    ) {
        projectService.changeProjectName(projectId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{projectId}/change-description")
    public ResponseEntity<Void> changeProjectDescription(
        @PathVariable Long projectId,
        @Valid @RequestBody ChangeProjectDescriptionRequest request
    ) {
        projectService.changeProjectDescription(projectId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
        @PathVariable Long projectId
    ) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
