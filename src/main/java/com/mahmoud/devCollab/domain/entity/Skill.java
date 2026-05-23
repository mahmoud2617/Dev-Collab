package com.mahmoud.devCollab.domain.entity;

import com.mahmoud.devCollab.domain.relation.UserSkill;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "skills")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "skill")
    private Set<UserSkill> users = new HashSet<>();
}
