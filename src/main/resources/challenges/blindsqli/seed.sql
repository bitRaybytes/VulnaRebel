-- The "hacker" has to read the secret
INSERT INTO blind_users (username, secret)
VALUES ('admin', 'ctf{Bl1nD_Bu7_N0t_D34f}')
ON CONFLICT DO NOTHING;