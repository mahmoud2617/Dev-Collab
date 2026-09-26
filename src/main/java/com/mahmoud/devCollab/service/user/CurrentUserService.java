package com.mahmoud.devCollab.service.user;

import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.exception.UnauthorizedUserException;
import com.mahmoud.devCollab.repository.UserRepository;
import com.mahmoud.devCollab.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        return userRepository.findById(currentUserId()).orElseThrow(UnauthorizedUserException::new);
    }

    public User getCurrentUserWithProfileAndSkills() {
        return userRepository.findByIdWithProfileAndSkills(currentUserId()).orElseThrow(UnauthorizedUserException::new);
    }

    public User getCurrentUserWithSkills() {
        return userRepository.findByIdWithSkills(currentUserId()).orElseThrow(UnauthorizedUserException::new);
    }

    private Long currentUserId() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            throw new UnauthorizedUserException();
        }

        return customUserDetails.getId();
    }
}
