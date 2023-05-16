USE jwt_user_db;

DROP TABLE IF EXISTS tokens;
DROP TABLE IF EXISTS refresh_tokens;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS security_users_roles;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS managers;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS security_users;

CREATE TABLE security_users
(
    id                  BIGINT NOT NULL AUTO_INCREMENT,
    email               VARCHAR(45)     NOT NULL UNIQUE,
    first_name          VARCHAR(45)     NOT NULL,
    last_name           VARCHAR(45)     NOT NULL,
    password            VARCHAR(100)    NOT NULL,
    time_insert         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY         (id)
) COLLATE utf8_bin;

INSERT INTO security_users (id, email, password)
VALUES (1, 'user1', '$2a$12$f66aaKBc6f23La7JaN5ca.obWwmx8ENNHG.pDNdgHTAkgxVEIOqgW'),
       (2, 'user2', '$2a$12$eV8SV478lxghvjeLDNbH.eUyd4AW.81SFTKjcViyJAOO0qkaJbiG2'),
       (3, 'user3', '$2y$10$/DGK1xfFrrnHaKswprgJVedHSHI9w3n.qrt3NF1rmuX7P9KhI7vZO');

CREATE TABLE roles
(
    id                  BIGINT NOT NULL     AUTO_INCREMENT,
    name                VARCHAR(45)         NOT NULL UNIQUE,
    PRIMARY KEY         (id)
) COLLATE utf8_bin;

INSERT INTO roles
VALUES (1, 'CLIENT'),
       (2, 'MANAGER'),
       (3, 'ADMIN');

CREATE TABLE security_users_roles
(
    user_id             BIGINT NOT NULL,
    role_id             BIGINT NOT NULL,
    PRIMARY KEY         (user_id, role_id)
) COLLATE utf8_bin;

INSERT INTO security_users_roles
VALUES (1, 1),
       (2, 2),
       (3, 3);

CREATE TABLE tokens
(
    id                  BIGINT NOT NULL     AUTO_INCREMENT,
    token               VARCHAR(256)        NOT NULL UNIQUE,
    sec_user_id         BIGINT,
    PRIMARY KEY         (id),
    FOREIGN KEY         (sec_user_id)       REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;

CREATE TABLE refresh_tokens
(
    id                  BIGINT NOT NULL     AUTO_INCREMENT,
    refresh_token       VARCHAR(256)        NOT NULL UNIQUE,
    sec_user_id         BIGINT,
    PRIMARY KEY         (id),
    FOREIGN KEY         (sec_user_id)       REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;

CREATE TABLE clients
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    email               VARCHAR(45)     NOT NULL,
    first_name          VARCHAR(45)     NOT NULL,
    last_name           VARCHAR(45)     NOT NULL,
    phone_number        VARCHAR(45)             ,
    city                VARCHAR(45)             ,
    time_insert         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sec_user_id         BIGINT,
    PRIMARY KEY         (id),
    FOREIGN KEY         (sec_user_id)   REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;
INSERT INTO clients (id, email, first_name, last_name, phone_number, city, sec_user_id)
VALUES (1, 'user1', 'name1', 'surname1', '+9981111111', 'Samarkand', 1);

CREATE TABLE managers
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    email               VARCHAR(45)     NOT NULL,
    first_name          VARCHAR(45)     NOT NULL,
    last_name           VARCHAR(45)     NOT NULL,
    phone_number        VARCHAR(45)             ,
    time_insert         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sec_user_id         BIGINT,
    PRIMARY KEY         (id),
    FOREIGN KEY         (sec_user_id)   REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;
INSERT INTO managers (id, email, first_name, last_name, phone_number, sec_user_id)
VALUES (1, 'user2', 'name2', 'surname2', '+9982222222', 2);

CREATE TABLE admins
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    email               VARCHAR(45)     NOT NULL,
    first_name          VARCHAR(45)     NOT NULL,
    last_name           VARCHAR(45)     NOT NULL,
    phone_number        VARCHAR(45)             ,
    time_insert         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sec_user_id         BIGINT,
    PRIMARY KEY         (id),
    FOREIGN KEY         (sec_user_id)   REFERENCES jwt_user_db.security_users(id)
) COLLATE utf8_bin;
INSERT INTO admins (id, email, first_name, last_name, phone_number, sec_user_id)
VALUES (1, 'user3', 'name3', 'surname3', '+9983333333', 3);
