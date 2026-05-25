package com.mahmoud.devCollab.repository;

import com.mahmoud.devCollab.domain.entity.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
