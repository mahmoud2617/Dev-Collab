package com.mahmoud.devCollab.event;

import com.mahmoud.devCollab.domain.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProfileUpdatedEvent {
    private Profile profile;
}
