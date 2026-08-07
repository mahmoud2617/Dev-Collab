package com.mahmoud.devCollab.repository;

import com.mahmoud.devCollab.domain.entity.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
}
