package com.mahmoud.devCollab.repository;

import com.mahmoud.devCollab.domain.relation.ProjectMember;
import org.springframework.data.repository.CrudRepository;

public interface ProjectMemberRepository extends CrudRepository<ProjectMember, Long> {
    boolean existsByProjectIdAndMemberId(Long projectId, Long memberId);
}
