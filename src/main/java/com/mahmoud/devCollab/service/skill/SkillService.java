package com.mahmoud.devCollab.service.skill;

import com.mahmoud.devCollab.domain.entity.Skill;
import com.mahmoud.devCollab.dto.CreateSkillRequest;
import com.mahmoud.devCollab.dto.EditSkillRequest;
import com.mahmoud.devCollab.dto.SkillDto;
import com.mahmoud.devCollab.exception.ConflictException;
import com.mahmoud.devCollab.exception.NotFoundException;
import com.mahmoud.devCollab.mapper.SkillMapper;
import com.mahmoud.devCollab.repository.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public Page<SkillDto> getAllSkills(int page, int size) {
        Pageable pageable = getPageable(page, size);

        return skillRepository.findAll(pageable)
                .map(skillMapper::toDto);
    }

    public Page<SkillDto> searchSkill(String skill, int page, int size) {
        Pageable pageable = getPageable(page, size);

        return skillRepository.searchSkills(skill, pageable)
                .map(skillMapper::toDto);
    }

    private Pageable getPageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size <= 0? 20 : Math.max(size, 100);

        return PageRequest.of(safePage, safeSize);
    }

    public SkillDto createSkill(CreateSkillRequest request) {
        String normalizedName = request.getName().trim().replaceAll("\\s+", " ");

        if (skillRepository.findByName(normalizedName).isPresent()) {
            throw new ConflictException("Skill already exists.");
        }

        Skill skill = skillRepository.save(new Skill(normalizedName));

        return skillMapper.toDto(skill);
    }

    public void editSkill(Long id, EditSkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Skill not found."));

        skill.setName(request.getNewName());
        skillRepository.save(skill);
    }

    public void deleteSkill(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Skill not found."));

        skillRepository.delete(skill);
    }
}
