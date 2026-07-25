CREATE TABLE IF NOT EXISTS products
(
    id          SMALLSERIAL PRIMARY KEY,
    name        VARCHAR(50),
    category    VARCHAR(50),
    price       INT,
    description VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS application_settings
(
    setting_key   VARCHAR(50),
    setting_value VARCHAR(100)
);

