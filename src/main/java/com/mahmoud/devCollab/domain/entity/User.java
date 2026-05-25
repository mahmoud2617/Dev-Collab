package com.mahmoud.devCollab.domain.entity;

import com.mahmoud.devCollab.domain.enums.Role;
import com.mahmoud.devCollab.domain.relation.ProjectMember;
import com.mahmoud.devCollab.domain.relation.UserSkill;
import com.mahmoud.devCollab.domain.relation.UserTask;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Role role;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Profile profile;

    @OneToMany(mappedBy = "user")
    private Set<VerificationToken> verificationTokens = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserSkill> skills = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<ProjectMember> memberships = new HashSet<>();

    @OneToMany(mappedBy = "assignedTo")
    private Set<UserTask> tasks = new HashSet<>();
}
