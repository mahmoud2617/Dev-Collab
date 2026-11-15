package com.mahmoud.devCollab.repository;

import com.mahmoud.devCollab.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = """
        SELECT p FROM Project p
        LEFT JOIN FETCH p.projectMembers
        WHERE p.id = :id""")
    Optional<Project> findByIdWithProjectMembers(@Param("id") Long id);
}
