package com.mahmoud.devCollab.service.skill;

import com.mahmoud.devCollab.domain.entity.Skill;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.relation.UserSkill;
import com.mahmoud.devCollab.dto.SkillDto;
import com.mahmoud.devCollab.exception.ConflictException;
import com.mahmoud.devCollab.exception.NotFoundException;
import com.mahmoud.devCollab.mapper.SkillMapper;
import com.mahmoud.devCollab.repository.SkillRepository;
import com.mahmoud.devCollab.service.user.CurrentUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserSkillService {
    private final CurrentUserService currentUserService;
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public List<SkillDto> getMySkills() {
        User user = currentUserService.getCurrentUserWithSkills();

        return user.getUserSkills()
                .stream()
                .map(UserSkill::getSkill)
                .map(skillMapper::toDto)
                .toList();
    }

    @Transactional
    public void addUserSkill(Long id) {
        User user = currentUserService.getCurrentUserWithSkills();

        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Skill not found."));

        boolean alreadyHasSkill = user.getUserSkills()
                .stream()
                .anyMatch(userSkill -> userSkill.getSkill().getId().equals(id));

        if (alreadyHasSkill) {
            throw new ConflictException("User already has this skill.");
        }

        UserSkill userSkill = new UserSkill();
        userSkill.setSkill(skill);
        userSkill.setUser(user);

        user.getUserSkills().add(userSkill);
        skill.getUserSkills().add(userSkill);
    }

    @Transactional
    public void deleteUserSkill(Long id) {
        User user = currentUserService.getCurrentUserWithSkills();

        UserSkill userSkill = user.getUserSkills()
                .stream()
                .filter(us -> us.getSkill().getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User doesn't have this skill."));

        user.getUserSkills().remove(userSkill);
        userSkill.getSkill().getUserSkills().remove(userSkill);
    }
}
