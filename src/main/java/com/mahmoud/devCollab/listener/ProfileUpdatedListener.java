package com.mahmoud.devCollab.listener;

import com.mahmoud.devCollab.domain.entity.Profile;
import com.mahmoud.devCollab.event.ProfileUpdatedEvent;
import com.mahmoud.devCollab.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ProfileUpdatedListener {
    private final ProfileRepository profileRepository;

    @EventListener
    public void handleProfileUpdatedEvent(ProfileUpdatedEvent event) {
        Profile profile = event.getProfile();

        profile.setUpdatedAt(LocalDateTime.now());
        profileRepository.save(profile);
    }
}
