package com.mahmoud.devCollab.controller;

import com.mahmoud.devCollab.dto.*;
import com.mahmoud.devCollab.service.project.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ProjectDto getProject(
        @PathVariable Long id
    ) {
        return projectService.getProjectDetails(id);
    }

    @PostMapping
    public ResponseEntity<CreateProjectResponse> createProject(
        @Valid @RequestBody CreateProjectRequest request
    ) {
        return ResponseEntity.status(201).body(projectService.createProject(request));
    }

    @PatchMapping("/{id}/change-name")
    public ResponseEntity<Void> changeProjectName(
        @PathVariable Long id,
        @Valid @RequestBody ChangeProjectNameRequest request
    ) {
        projectService.changeProjectName(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/change-description")
    public ResponseEntity<Void> changeProjectDescription(
        @PathVariable Long id,
        @Valid @RequestBody ChangeProjectDescriptionRequest request
    ) {
        projectService.changeProjectDescription(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
        @PathVariable Long id
    ) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
