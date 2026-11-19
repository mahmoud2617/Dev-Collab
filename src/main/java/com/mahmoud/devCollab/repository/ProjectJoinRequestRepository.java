package com.mahmoud.devCollab.repository;

import com.mahmoud.devCollab.domain.entity.ProjectJoinRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProjectJoinRequestRepository extends CrudRepository<ProjectJoinRequest, Long> {
    @Query(value = """
        SELECT pjr FROM ProjectJoinRequest pjr
        LEFT JOIN FETCH pjr.user
        WHERE pjr.project.id = :projectId
        ORDER BY pjr.sentAt""")
    List<ProjectJoinRequest> findAllByProjectId(Long projectId);

    ProjectJoinRequest findByProjectIdAndUserId(Long projectId, Long userId);

    boolean existsByProjectIdAndUserId(Long projectId, Long id);
}
