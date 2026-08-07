package com.mahmoud.devCollab.controller;

import com.mahmoud.devCollab.dto.ChangePasswordRequest;
import com.mahmoud.devCollab.dto.ChangeUsernameRequest;
import com.mahmoud.devCollab.dto.UserDto;
import com.mahmoud.devCollab.security.annotation.IsSelfOrAdmin;
import com.mahmoud.devCollab.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(
        Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/me")
    public UserDto getMe() {
        return userService.getMe();
    }

    @PatchMapping("/me/username")
    public ResponseEntity<Void> changeUsername(
        @Valid @RequestBody ChangeUsernameRequest request
    ) {
        userService.changeUsername(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(
        @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    @IsSelfOrAdmin
    public ResponseEntity<Void> deleteUser(
        @PathVariable Long userId,
        HttpServletResponse response
    ) {
        userService.deleteUser(userId, response);
        return ResponseEntity.noContent().build();
    }
}
