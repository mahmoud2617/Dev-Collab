package com.mahmoud.devCollab.domain.entity;

import com.mahmoud.devCollab.domain.relation.ProjectTechStack;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tech_stack")
public class TechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tech_stack")
    private String techStack;

    @OneToMany(mappedBy = "techStack")
    private Set<ProjectTechStack> projects = new HashSet<>();
}
