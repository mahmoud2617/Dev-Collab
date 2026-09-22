package com.mahmoud.devCollab.repository;

import com.mahmoud.devCollab.domain.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    @Query(value = """
        SELECT s FROM Skill s
        WHERE s.name ILIKE CONCAT('%', :skill, '%')""")
    Page<Skill> searchSkills(
        @Param("skill") String skill,
        Pageable pageable
    );

    Optional<Skill> findByName(String skill);
}
