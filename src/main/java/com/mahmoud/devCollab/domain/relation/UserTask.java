package com.mahmoud.devCollab.domain.relation;

import com.mahmoud.devCollab.domain.entity.Task;
import com.mahmoud.devCollab.domain.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "users_tasks")
public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
