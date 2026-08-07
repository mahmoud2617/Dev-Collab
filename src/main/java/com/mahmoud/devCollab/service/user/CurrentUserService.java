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
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            throw new UnauthorizedUserException();
        }

        return userRepository.findById(customUserDetails.getId()).orElseThrow(UnauthorizedUserException::new);
    }
}
