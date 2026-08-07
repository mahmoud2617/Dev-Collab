package com.mahmoud.devCollab.service.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mahmoud.devCollab.domain.entity.Profile;
import com.mahmoud.devCollab.domain.entity.ProfilePicture;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.dto.ChangeBioRequest;
import com.mahmoud.devCollab.dto.ProfileDto;
import com.mahmoud.devCollab.event.ProfileUpdatedEvent;
import com.mahmoud.devCollab.exception.InternalServerErrorException;
import com.mahmoud.devCollab.exception.InvalidRequestDataException;
import com.mahmoud.devCollab.mapper.ProfileMapper;
import com.mahmoud.devCollab.repository.ProfilePictureRepository;
import com.mahmoud.devCollab.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProfileService {
    private final CurrentUserService currentUserService;
    private final ProfileRepository profileRepository;
    private final ProfilePictureRepository profilePictureRepository;
    private final ProfileMapper profileMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final Cloudinary cloudinary;

    public ProfileDto getMyProfile() {
        User user = currentUserService.getCurrentUser();

        Profile profile = profileRepository.findByUserIdWithSkills(user.getId());

        return profileMapper.toDto(profile);
    }

    @Transactional
    public void changeProfilePicture(MultipartFile picture) {
        if (picture.getContentType() == null || !picture.getContentType().startsWith("image/")) {
            throw new InvalidRequestDataException("Profile picture must be an image.");
        }

        /* I handled it in application.yaml to be the max file size 10 MB but here i only want 5 MB,
        So i added this bc i can handle the error message to let the frontend know (from the thrown error message)
        the actual size that i want */
        if (picture.getSize() > 5 * 1024 * 1024) {
            throw new InvalidRequestDataException("Profile picture size must be less than 5MB.");
        }

        Map<String, String> allowedTypes = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp"
        );

        if (!allowedTypes.containsKey(picture.getContentType())) {
            throw new InvalidRequestDataException("Unsupported picture type.");
        }

        if (!allowedTypes.containsKey(getActualType(picture))) {
            throw new InvalidRequestDataException("The actual picture type not the same as the MIME type.");
        }

        Profile profile = profileRepository.findByUserId(currentUserService.getCurrentUser().getId());

        try {
            if (profile.getProfilePicture() != null) {
                try {
                    cloudinary.uploader().destroy(profile.getProfilePicture().getPublicId(), ObjectUtils.emptyMap());
                } catch (IOException e) {
                    throw new InternalServerErrorException("Failed to delete old profile picture.");
                }
            }

            Map<?, ?> response = cloudinary.uploader().upload(
                picture.getBytes(),
                ObjectUtils.emptyMap()
            );

            ProfilePicture profilePicture = ProfilePicture.builder()
                    .publicId(response.get("public_id").toString())
                    .url(response.get("secure_url").toString())
                    .build();

            profile.setProfilePicture(profilePicture);

            profilePictureRepository.save(profilePicture);
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to upload profile picture.");
        }

        eventPublisher.publishEvent(new ProfileUpdatedEvent(profile));
    }

    @Transactional
    public void changeBio(ChangeBioRequest request) {
        String bio = request.getBio();

        if (bio != null) {
            bio = request.getBio().trim();

            if (bio.isEmpty()) {
                throw new InvalidRequestDataException("Bio is required.");
            }
        }

        Profile profile = profileRepository.findByUserId(currentUserService.getCurrentUser().getId());

        profile.setBio(bio);

        eventPublisher.publishEvent(new ProfileUpdatedEvent(profile));
    }

    public void deleteProfilePicture() {
        Profile profile = profileRepository.findByUserId(currentUserService.getCurrentUser().getId());

        if (profile.getProfilePicture() == null) {
            return;
        }

        try {
            cloudinary.uploader().destroy(profile.getProfilePicture().getPublicId(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to delete profile picture.");
        }

        profile.setProfilePicture(null);

        eventPublisher.publishEvent(new ProfileUpdatedEvent(profile));
    }

    private String getActualType(MultipartFile file) {
        try {
            Tika tika = new Tika();

            return tika.detect(file.getInputStream());
        } catch (Exception e) {
            return null;
        }
    }
}
