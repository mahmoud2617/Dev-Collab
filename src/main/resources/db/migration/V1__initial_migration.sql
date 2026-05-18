CREATE SCHEMA IF NOT EXISTS dev_collab;

CREATE TYPE dev_collab.role AS ENUM ('ADMIN', 'USER');
CREATE TYPE dev_collab.verification_token_type AS ENUM ('EMAIL_VERIFICATION', 'PASSWORD_RESET', 'PASSWORD_RESET_SESSION');
CREATE TYPE dev_collab.task_status AS ENUM ('TODO', 'IN_PROGRESS', 'DONE');
CREATE TYPE dev_collab.member_role AS ENUM ('OWNER', 'ADMIN', 'MEMBER');

CREATE TABLE dev_collab.users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(150) NOT NULL UNIQUE,
    email VARCHAR(250) NOT NULL UNIQUE,
    password VARCHAR(250) NOT NULL UNIQUE,
    role dev_collab.role NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    bio TEXT
);

CREATE TABLE dev_collab.verification_tokens (
    id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(64) NOT NULL,
    token_type dev_collab.verification_token_type NOT NULL,
    expiration_date TIMESTAMP NOT NULL,

    CONSTRAINT auth_tokens_users_fk
        FOREIGN KEY (user_id) REFERENCES dev_collab.users (id)
        ON DELETE CASCADE
);

CREATE TABLE dev_collab.skills (
    id BIGSERIAL PRIMARY KEY,
    skill VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE dev_collab.users_skills (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,

    CONSTRAINT user_skill_users_fk
        FOREIGN KEY (user_id) REFERENCES dev_collab.users (id)
        ON DELETE CASCADE,

    CONSTRAINT user_skill_skills_fk
        FOREIGN KEY (skill_id) REFERENCES dev_collab.skills (id)
        ON DELETE CASCADE,

    CONSTRAINT unique_user_skill
        UNIQUE (user_id, skill_id)
);

CREATE TABLE dev_collab.projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT projects_users_fk
        FOREIGN KEY (owner_id) REFERENCES dev_collab.users (id)
        ON DELETE CASCADE
);

CREATE TABLE dev_collab.tech_stacks (
    id BIGSERIAL PRIMARY KEY,
    tech_stack VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE dev_collab.projects_tech_stacks (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    tech_stack_id BIGINT NOT NULL,

    CONSTRAINT project_tech_stack_projects_fk
        FOREIGN KEY (project_id) REFERENCES dev_collab.projects (id)
        ON DELETE CASCADE,

    CONSTRAINT project_tech_stack_tech_stacks_fk
        FOREIGN KEY (tech_stack_id) REFERENCES dev_collab.tech_stacks (id)
        ON DELETE CASCADE,

    CONSTRAINT unique_project_tech_stack
        UNIQUE (project_id, tech_stack_id)
);

CREATE TABLE dev_collab.members (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    role dev_collab.member_role NOT NULL,

    CONSTRAINT members_projects_fk
        FOREIGN KEY (project_id) REFERENCES dev_collab.projects(id)
        ON DELETE CASCADE,

    CONSTRAINT members_users_fk
        FOREIGN KEY (member_id) REFERENCES dev_collab.users(id)
        ON DELETE CASCADE,

    CONSTRAINT unique_project_member
        UNIQUE (project_id, member_id)
);

CREATE TABLE dev_collab.tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status dev_collab.task_status NOT NULL,
    created_by BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT tasks_users_fk
        FOREIGN KEY (created_by) REFERENCES dev_collab.users(id),

    CONSTRAINT tasks_projects_fk
        FOREIGN KEY (project_id) REFERENCES dev_collab.projects(id)
        ON DELETE CASCADE
);

CREATE TABLE dev_collab.users_tasks (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,

    CONSTRAINT users_tasks_users_fk
        FOREIGN KEY (user_id) REFERENCES dev_collab.users(id),

    CONSTRAINT users_tasks_tasks_fk
        FOREIGN KEY (task_id) REFERENCES dev_collab.tasks(id),

    CONSTRAINT unique_user_task
        UNIQUE (task_id, user_id)
);

CREATE TABLE dev_collab.files (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,
    project_id BIGINT NOT NULL,
    task_id BIGINT,
    uploaded_by BIGINT NOT NULL,
    size INT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL,

    CONSTRAINT files_projects_fk
        FOREIGN KEY (project_id) REFERENCES dev_collab.projects(id)
        ON DELETE CASCADE,

    CONSTRAINT files_tasks_fk
        FOREIGN KEY (task_id) REFERENCES dev_collab.tasks(id)
        ON DELETE CASCADE,

    CONSTRAINT files_users_fk
        FOREIGN KEY (uploaded_by) REFERENCES dev_collab.users(id)
);

CREATE TABLE dev_collab.chats (
    id UUID PRIMARY KEY,
    project_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL,

    CONSTRAINT chats_projects_fk
        FOREIGN KEY (project_id) REFERENCES dev_collab.projects(id)
        ON DELETE CASCADE,

    CONSTRAINT chats_users_fk
        FOREIGN KEY (sender_id) REFERENCES dev_collab.users(id)
);

CREATE TABLE dev_collab.activity_log (
    id UUID PRIMARY KEY,
    project_id BIGINT NOT NULL,
    made_by BIGINT NOT NULL,
    action TEXT NOT NULL,
    made_at TIMESTAMP NOT NULL,

    CONSTRAINT activity_log_projects_fk
        FOREIGN KEY (project_id) REFERENCES dev_collab.projects(id)
        ON DELETE CASCADE,

    CONSTRAINT activity_log_users_fk
        FOREIGN KEY (made_by) REFERENCES dev_collab.users(id)
);
