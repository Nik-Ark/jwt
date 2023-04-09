CREATE TABLE IF NOT EXISTS app_users
(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    first_name      VARCHAR(45)     NOT NULL,
    last_name       VARCHAR(45)     NOT NULL,
    time_insert     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY     (id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS security_users
(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    email           VARCHAR(45)     NOT NULL UNIQUE,
    password        VARCHAR(100)    NOT NULL,
    time_insert     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    app_user_id     BIGINT,
    PRIMARY KEY     (id),
    FOREIGN KEY     (app_user_id)   REFERENCES jwt_user_db.app_users(id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS roles
(
    id              BIGINT NOT NULL AUTO_INCREMENT,
    name            VARCHAR(45)     NOT NULL UNIQUE,
    PRIMARY KEY     (id)
) COLLATE utf8_bin;

-- INSERT INTO roles
-- VALUES (1, 'CLIENT'),
--        (2, 'MANAGER'),
--        (3, 'ADMIN'),
--        (4, 'DEVELOPER');

CREATE TABLE IF NOT EXISTS security_users_roles
(
    user_id         BIGINT NOT NULL,
    role_id         BIGINT NOT NULL,
    PRIMARY KEY     (user_id, role_id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS tokens
(
    id                  BIGINT NOT NULL     AUTO_INCREMENT,
    token               VARCHAR(256)        NOT NULL UNIQUE,
    revoked             BOOLEAN             DEFAULT FALSE,
    expiry_date         TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    security_user_id    BIGINT,
    PRIMARY KEY         (id),
    FOREIGN KEY         (security_user_id)  REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id                  BIGINT NOT NULL     AUTO_INCREMENT,
    refresh_token       VARCHAR(256)        NOT NULL UNIQUE,
    revoked             BOOLEAN             DEFAULT FALSE,
    expiry_date         TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    security_user_id    BIGINT,
    PRIMARY KEY         (id),
    FOREIGN KEY         (security_user_id)  REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;
