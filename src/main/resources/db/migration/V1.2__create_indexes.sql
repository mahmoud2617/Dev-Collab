CREATE INDEX idx_skill_name
    ON skills (name);

CREATE INDEX idx_projects_join_requests_project_id
    ON projects_join_requests (project_id);