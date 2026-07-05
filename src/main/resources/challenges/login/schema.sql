create table login_users
(
    id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(25),
    password VARCHAR(25),
    role VARCHAR(25)
)