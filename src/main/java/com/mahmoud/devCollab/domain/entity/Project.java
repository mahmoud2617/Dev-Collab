package com.mahmoud.devCollab.domain.entity;

import com.mahmoud.devCollab.domain.relation.ProjectMember;
import com.mahmoud.devCollab.domain.relation.ProjectTechStack;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "project")
    private Set<ProjectTechStack> techStacks = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<File> files = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<ProjectMember> projectMembers = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<ActivityLog> activityLog = new ArrayList<>();
}
