package com.mahmoud.devCollab.controller;

import com.mahmoud.devCollab.dto.SkillDto;
import com.mahmoud.devCollab.service.skill.UserSkillService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/me/skills")
@AllArgsConstructor
public class UserSkillController {
    private final UserSkillService userSkillService;

    @GetMapping
    public List<SkillDto> getMySkills() {
        return userSkillService.getMySkills();
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> addUserSkill(
        @PathVariable Long id
    ) {
        userSkillService.addUserSkill(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSkill(
        @PathVariable Long id
    ) {
        userSkillService.deleteUserSkill(id);
        return ResponseEntity.noContent().build();
    }
}
