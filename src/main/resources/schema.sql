CREATE TABLE IF NOT EXISTS security_users
(
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    email           VARCHAR(45)     NOT NULL UNIQUE,
    first_name      VARCHAR(45)     NOT NULL,
    last_name       VARCHAR(45)     NOT NULL,
    password        VARCHAR(100)    NOT NULL,
    time_insert     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY     (id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS roles
(
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    name            VARCHAR(45)     NOT NULL UNIQUE,
    PRIMARY KEY     (id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS security_users_roles
(
    user_id         BIGINT NOT NULL,
    role_id         BIGINT NOT NULL,
    PRIMARY KEY     (user_id, role_id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS tokens
(
    id                  BIGINT              NOT NULL AUTO_INCREMENT,
    token               VARCHAR(256)        NOT NULL UNIQUE,
    sec_user_id         BIGINT,
    PRIMARY KEY         (id),
    FOREIGN KEY         (sec_user_id)       REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id                  BIGINT              NOT NULL AUTO_INCREMENT,
    refresh_token       VARCHAR(256)        NOT NULL UNIQUE,
    sec_user_id         BIGINT,
    PRIMARY KEY         (id),
    FOREIGN KEY         (sec_user_id)       REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS clients
(
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    email           VARCHAR(45)     NOT NULL,
    first_name      VARCHAR(45)     NOT NULL,
    last_name       VARCHAR(45)     NOT NULL,
    phone_number    VARCHAR(45)             ,
    city            VARCHAR(45)             ,
    time_insert     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sec_user_id     BIGINT,
    PRIMARY KEY     (id),
    FOREIGN KEY     (sec_user_id)   REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS managers
(
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    email           VARCHAR(45)     NOT NULL,
    first_name      VARCHAR(45)     NOT NULL,
    last_name       VARCHAR(45)     NOT NULL,
    phone_number    VARCHAR(45)             ,
    time_insert     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sec_user_id     BIGINT,
    PRIMARY KEY     (id),
    FOREIGN KEY     (sec_user_id)   REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS admins
(
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    email           VARCHAR(45)     NOT NULL,
    first_name      VARCHAR(45)     NOT NULL,
    last_name       VARCHAR(45)     NOT NULL,
    phone_number    VARCHAR(45)             ,
    time_insert     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sec_user_id     BIGINT,
    PRIMARY KEY     (id),
    FOREIGN KEY     (sec_user_id)   REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;
