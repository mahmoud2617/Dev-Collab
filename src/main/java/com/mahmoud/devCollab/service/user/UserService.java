package com.mahmoud.devCollab.service.user;

import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.dto.ChangePasswordRequest;
import com.mahmoud.devCollab.dto.ChangeUsernameRequest;
import com.mahmoud.devCollab.dto.UserDto;
import com.mahmoud.devCollab.event.ProfileUpdatedEvent;
import com.mahmoud.devCollab.exception.InvalidRequestDataException;
import com.mahmoud.devCollab.exception.UnauthorizedUserException;
import com.mahmoud.devCollab.mapper.UserMapper;
import com.mahmoud.devCollab.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public Page<UserDto> getAllUsers(Pageable pageable) {
        int pageSize = Math.min(pageable.getPageSize(), 100);

        Sort.Order order = pageable.getSort()
                .stream()
                .findFirst()
                .orElse(Sort.Order.by("id"));

        String sortProperty = order.getProperty();
        Sort.Direction direction = order.getDirection();

        List<String> allowedSortProperties = List.of("id", "username", "email");

        if (!allowedSortProperties.contains(sortProperty)) {
            sortProperty = "id";
        }

        pageable = PageRequest.of(pageable.getPageNumber(), pageSize, Sort.by(direction, sortProperty));

        Page<User> users = userRepository.findAll(pageable);

        return users.map(userMapper::toDto);
    }

    public UserDto getMe() {
        User user = currentUserService.getCurrentUser();

        return userMapper.toDto(user);
    }

    @Transactional
    public void changeUsername(ChangeUsernameRequest request) {
        User user = userRepository.findById(currentUserService.getCurrentUser().getId())
                .orElseThrow(UnauthorizedUserException::new);

        if (user.getUsername().equals(request.getNewUsername())) {
            throw new InvalidRequestDataException("New username cannot be the same as the current username.");
        }

        if (userRepository.existsByUsername(request.getNewUsername())) {
            throw new InvalidRequestDataException("Username is already taken.");
        }

        user.setUsername(request.getNewUsername());

        eventPublisher.publishEvent(new ProfileUpdatedEvent(user.getProfile()));
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = userRepository.findById(currentUserService.getCurrentUser().getId())
                .orElseThrow(UnauthorizedUserException::new);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidRequestDataException("Current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public void deleteUser(Long userId, HttpServletResponse response) {
        userRepository.deleteById(userId);

        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/auth");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
