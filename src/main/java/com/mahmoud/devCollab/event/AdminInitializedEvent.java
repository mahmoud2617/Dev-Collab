package com.mahmoud.devCollab.event;

import com.mahmoud.devCollab.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AdminInitializedEvent {
    private User admin;
}
