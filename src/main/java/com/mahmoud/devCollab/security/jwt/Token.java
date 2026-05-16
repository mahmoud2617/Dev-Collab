package com.mahmoud.devCollab.security.jwt;

import com.mahmoud.devCollab.domain.enums.Role;

public record Token(
    Long id,
    String username,
    String email,
    Role role
) {}
