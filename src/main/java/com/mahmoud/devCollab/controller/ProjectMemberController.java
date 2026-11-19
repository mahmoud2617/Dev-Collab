package com.mahmoud.devCollab.controller;

import com.mahmoud.devCollab.dto.ChangeMemberRoleInProjectRequest;
import com.mahmoud.devCollab.service.project.ProjectMemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/members")
@AllArgsConstructor
@Validated
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @PostMapping // no join request needed.
    public ResponseEntity<Void> addUserToProject(
        @PathVariable Long projectId,
        @RequestBody @NotNull @Positive Long userId
    ) {
        projectMemberService.addUserToProject(projectId, userId);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<Void> changeMemberRoleInTheProject(
        @PathVariable Long projectId,
        @PathVariable Long memberId,
        @Valid @RequestBody ChangeMemberRoleInProjectRequest request
    ) {
        projectMemberService.changeMemberRoleInTheProject(projectId, memberId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeMemberFromProject(
        @PathVariable Long projectId,
        @PathVariable Long memberId
    ) {
        projectMemberService.removeMemberFromProject(projectId, memberId);
        return ResponseEntity.noContent().build();
    }
}
