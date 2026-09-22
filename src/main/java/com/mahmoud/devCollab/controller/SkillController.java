package com.mahmoud.devCollab.controller;

import com.mahmoud.devCollab.dto.CreateSkillRequest;
import com.mahmoud.devCollab.dto.EditSkillRequest;
import com.mahmoud.devCollab.dto.SkillDto;
import com.mahmoud.devCollab.service.skill.SkillService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/skills")
@AllArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @GetMapping
    public Page<SkillDto> getSkills(
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        if (search == null || search.isBlank()) {
            return skillService.getAllSkills(page, size);
        }

        return skillService.searchSkill(search, page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SkillDto> createSkill(
        @Valid @RequestBody CreateSkillRequest request
    ) {
        return ResponseEntity.status(201).body(skillService.createSkill(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> editSkill(
        @PathVariable Long id,
        @Valid @RequestBody EditSkillRequest request
    ) {
        skillService.editSkill(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(
        @PathVariable Long id
    ) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
