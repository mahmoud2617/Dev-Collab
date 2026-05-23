package com.mahmoud.devCollab.domain.relation;

import com.mahmoud.devCollab.domain.entity.Skill;
import com.mahmoud.devCollab.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_skills")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
