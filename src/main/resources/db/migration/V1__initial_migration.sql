CREATE TYPE role AS ENUM ('ADMIN', 'USER');
CREATE TYPE verification_token_type AS ENUM ('EMAIL_VERIFICATION', 'PASSWORD_RESET', 'PASSWORD_RESET_SESSION');
CREATE TYPE task_status AS ENUM ('TODO', 'IN_PROGRESS', 'DONE');
CREATE TYPE member_role AS ENUM ('OWNER', 'ADMIN', 'MEMBER');

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(150) NOT NULL UNIQUE,
    email VARCHAR(250) NOT NULL UNIQUE,
    password VARCHAR(250) NOT NULL,
    role role NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    profile_picture_url TEXT,
    bio VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,

    CONSTRAINT profiles_users_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE TABLE verification_tokens (
    id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(64) NOT NULL,
    token_type verification_token_type NOT NULL,
    expiration_date TIMESTAMP NOT NULL,

    CONSTRAINT verification_tokens_users_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL,
    refresh_token TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,

    CONSTRAINT refresh_tokens_users_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE TABLE skills (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_skills (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,

    CONSTRAINT user_skill_users_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE,

    CONSTRAINT user_skill_skills_fk
        FOREIGN KEY (skill_id) REFERENCES skills (id)
        ON DELETE CASCADE,

    CONSTRAINT unique_user_skill
        UNIQUE (user_id, skill_id)
);

CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT projects_users_fk
        FOREIGN KEY (owner_id) REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE TABLE tech_stacks (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE project_tech_stacks (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    tech_stack_id BIGINT NOT NULL,

    CONSTRAINT project_tech_stack_projects_fk
        FOREIGN KEY (project_id) REFERENCES projects (id)
        ON DELETE CASCADE,

    CONSTRAINT project_tech_stack_tech_stacks_fk
        FOREIGN KEY (tech_stack_id) REFERENCES tech_stacks (id)
        ON DELETE CASCADE,

    CONSTRAINT unique_project_tech_stack
        UNIQUE (project_id, tech_stack_id)
);

CREATE TABLE project_members (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    role member_role NOT NULL,

    CONSTRAINT project_members_projects_fk
        FOREIGN KEY (project_id) REFERENCES projects(id)
        ON DELETE CASCADE,

    CONSTRAINT project_members_users_fk
        FOREIGN KEY (member_id) REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT unique_project_member
        UNIQUE (project_id, member_id)
);

CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status task_status NOT NULL,
    created_by BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT tasks_users_fk
        FOREIGN KEY (created_by) REFERENCES users(id),

    CONSTRAINT tasks_projects_fk
        FOREIGN KEY (project_id) REFERENCES projects(id)
        ON DELETE CASCADE
);

CREATE TABLE user_tasks (
    id BIGSERIAL PRIMARY KEY,
    assigned_by BIGINT NOT NULL,
    assigned_to BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,

    CONSTRAINT assigned_by_users_tasks_users_fk
        FOREIGN KEY (assigned_by) REFERENCES users(id),

    CONSTRAINT assigned_to_users_tasks_users_fk
        FOREIGN KEY (assigned_to) REFERENCES users(id),

    CONSTRAINT users_tasks_tasks_fk
        FOREIGN KEY (task_id) REFERENCES tasks(id),

    CONSTRAINT unique_user_task
        UNIQUE (task_id, assigned_to)
);

CREATE TABLE files (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,
    project_id BIGINT NOT NULL,
    task_id BIGINT,
    uploaded_by BIGINT NOT NULL,
    size BIGINT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL,

    CONSTRAINT files_projects_fk
        FOREIGN KEY (project_id) REFERENCES projects(id)
        ON DELETE CASCADE,

    CONSTRAINT files_tasks_fk
        FOREIGN KEY (task_id) REFERENCES tasks(id)
        ON DELETE CASCADE,

    CONSTRAINT files_users_fk
        FOREIGN KEY (uploaded_by) REFERENCES users(id)
);

CREATE TABLE messages (
    id UUID PRIMARY KEY,
    project_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL,

    CONSTRAINT messages_projects_fk
        FOREIGN KEY (project_id) REFERENCES projects(id)
        ON DELETE CASCADE,

    CONSTRAINT messages_users_fk
        FOREIGN KEY (sender_id) REFERENCES users(id)
);

CREATE TABLE activity_log (
    id UUID PRIMARY KEY,
    project_id BIGINT NOT NULL,
    made_by BIGINT NOT NULL,
    action TEXT NOT NULL,
    made_at TIMESTAMP NOT NULL,

    CONSTRAINT activity_log_projects_fk
        FOREIGN KEY (project_id) REFERENCES projects(id)
        ON DELETE CASCADE,

    CONSTRAINT activity_log_users_fk
        FOREIGN KEY (made_by) REFERENCES users(id)
);
