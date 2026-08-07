package com.mahmoud.devCollab.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profile_picture")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfilePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(name = "public_id")
    private String publicId;

    @Column(name = "url")
    private String url;

    @JsonIgnore
    @OneToOne(mappedBy = "profilePicture")
    private Profile profile;
}
