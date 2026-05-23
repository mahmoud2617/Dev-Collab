package com.mahmoud.devCollab.domain.entity;

import com.mahmoud.devCollab.domain.relation.ProjectTechStack;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tech_stacks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "techStack")
    private Set<ProjectTechStack> projects = new HashSet<>();
}
