package com.mahmoud.devCollab.security;

import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.exception.UserNotFoundException;
import com.mahmoud.devCollab.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        User user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() ->
                    userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(UserNotFoundException::new)
                );

        return new CustomUserDetails(user);
    }
}
