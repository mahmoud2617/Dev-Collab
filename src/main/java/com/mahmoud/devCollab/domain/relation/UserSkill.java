package com.mahmoud.devCollab.domain.relation;

import com.mahmoud.devCollab.domain.entity.Skill;
import com.mahmoud.devCollab.domain.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "user_skill")
public class UserSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
}
