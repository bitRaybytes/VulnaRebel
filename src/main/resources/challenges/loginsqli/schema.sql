CREATE TABLE IF NOT EXISTS login_users
(
    id       SMALLSERIAL PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50),
    role     VARCHAR(25)
);

-- ON CONFLICT DO NOTHING  requires  knowing  what
-- constitutes a conflict – which column is unique:
-- username as natural candidate:
ALTER TABLE login_users ADD CONSTRAINT login_users_username_unique UNIQUE (username);