package com.mahmoud.devCollab.controller;

import com.mahmoud.devCollab.dto.ChangeMemberRoleInProjectRequest;
import com.mahmoud.devCollab.service.project.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/members")
@AllArgsConstructor
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @PostMapping("/{id}")
    public void addMemberToProject(
        @PathVariable Long projectId,
        @PathVariable Long id
    ) {
        projectMemberService.addMember(projectId, id);
    }

    @PatchMapping("/{id}")
    public void changeMemberRoleInTheProject(
        @PathVariable Long projectId,
        @PathVariable Long id,
        @Valid @RequestBody ChangeMemberRoleInProjectRequest request
    ) {
        projectMemberService.changeMemberRoleInTheProject(projectId, id, request);
    }

    @DeleteMapping("/{id}")
    public void removeMemberFromProject(
        @PathVariable Long projectId,
        @PathVariable Long id
    ) {
        projectMemberService.removeMemberFromProject(projectId, id);
    }
}
