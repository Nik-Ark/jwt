CREATE TABLE IF NOT EXISTS security_users
(
    id              INT NOT NULL    AUTO_INCREMENT,
    username        VARCHAR(45)     NOT NULL UNIQUE,
    password        VARCHAR(100)    NOT NULL,
    time_insert     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY     (id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS authorities
(
    id              INT NOT NULL    AUTO_INCREMENT,
    name            VARCHAR(45)     NOT NULL,
    PRIMARY KEY     (id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS security_users_authorities
(
    user_id         INT NOT NULL,
    authority_id    INT NOT NULL,
    PRIMARY KEY     (user_id, authority_id)
) COLLATE utf8_bin;