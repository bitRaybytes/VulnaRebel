CREATE TABLE IF NOT EXISTS storedxss_guestbook (
    id        SMALLSERIAL PRIMARY KEY,
    author    VARCHAR(100),
    message   VARCHAR(1000)
);
