INSERT INTO login_users (username, password, role)
VALUES
    ('admin', 'superSecret123', 'admin'),
    ('john', 'password', 'user')
ON CONFLICT DO NOTHING;