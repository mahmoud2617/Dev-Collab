package com.mahmoud.devCollab.domain.entity;

import com.mahmoud.devCollab.domain.base.BaseUuidEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityLog extends BaseUuidEntity {
    @Column(name = "action")
    private String action;

    @Column(name = "made_at")
    private LocalDateTime madeAt;

    @ManyToOne
    @JoinColumn(name = "made_by")
    private User madeBy;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
