package com.mahmoud.devCollab.domain.relation;

import com.mahmoud.devCollab.domain.entity.Project;
import com.mahmoud.devCollab.domain.entity.User;
import com.mahmoud.devCollab.domain.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "project_members")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private MemberRole role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private User member;
}
