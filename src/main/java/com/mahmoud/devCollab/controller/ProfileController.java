package com.mahmoud.devCollab.controller;

import com.mahmoud.devCollab.dto.ChangeBioRequest;
import com.mahmoud.devCollab.dto.ProfileDto;
import com.mahmoud.devCollab.service.user.ProfileService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profiles")
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/me")
    public ProfileDto getMyProfile() {
        return profileService.getMyProfile();
    }

    @PatchMapping("/me/profile-picture")
    public ResponseEntity<Void> changeProfilePicture(
        @RequestParam MultipartFile picture
    ) {
        profileService.changeProfilePicture(picture);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/bio")
    public ResponseEntity<Void> changeBio(
        @Valid @RequestBody ChangeBioRequest request
    ) {
        profileService.changeBio(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me/profile-picture")
    public ResponseEntity<Void> deleteProfilePicture() {
        profileService.deleteProfilePicture();
        return ResponseEntity.noContent().build();
    }
}
