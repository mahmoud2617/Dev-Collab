package com.mahmoud.devCollab.repository;

import com.mahmoud.devCollab.domain.entity.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    @Query(value = """
        SELECT p FROM Profile p
        JOIN FETCH p.user u
        LEFT JOIN FETCH u.skills
        WHERE u.id = :id""")
    Profile findByUserIdWithSkills(@Param("id") Long id);

    Profile findByUserId(Long id);
}
