package com.mahmoud.devCollab.domain.relation;

import com.mahmoud.devCollab.domain.entity.Project;
import com.mahmoud.devCollab.domain.entity.TechStack;
import jakarta.persistence.*;

@Entity
@Table(name = "project_tech_stack")
public class ProjectTechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "tech_stack_id")
    private TechStack techStack;

}
