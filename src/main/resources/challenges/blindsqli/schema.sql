CREATE TABLE IF NOT EXISTS blind_users (
    id       SMALLSERIAL PRIMARY KEY,
    username VARCHAR(50),
    secret   VARCHAR(100)
);