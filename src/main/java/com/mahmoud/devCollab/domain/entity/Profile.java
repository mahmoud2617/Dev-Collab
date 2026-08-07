package com.mahmoud.devCollab.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profile {
    @Id
    private Long id;

    @Column(name = "bio")
    private String bio;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "profile_picture_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProfilePicture profilePicture;

}
