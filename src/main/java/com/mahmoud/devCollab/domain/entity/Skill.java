package com.mahmoud.devCollab.domain.entity;

import com.mahmoud.devCollab.domain.relation.UserSkill;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill")
    private String skill;

    @OneToMany(mappedBy = "skill")
    private Set<UserSkill> users = new HashSet<>();
}
