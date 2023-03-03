USE jwt_user_db;

DROP TABLE IF EXISTS security_users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS security_users_authorities;
DROP TABLE IF EXISTS app_users;

CREATE TABLE security_users
(
    id              INT NOT NULL    AUTO_INCREMENT,
    email           VARCHAR(45)     NOT NULL UNIQUE,
    password        VARCHAR(100)    NOT NULL,
    time_insert     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY     (id)
) COLLATE utf8_bin;

INSERT INTO security_users (id, email, password)
VALUES (1, 'user1', '$2a$12$f66aaKBc6f23La7JaN5ca.obWwmx8ENNHG.pDNdgHTAkgxVEIOqgW'),
       (2, 'user2', '$2a$12$eV8SV478lxghvjeLDNbH.eUyd4AW.81SFTKjcViyJAOO0qkaJbiG2'),
       (3, 'user3', '$2y$10$/DGK1xfFrrnHaKswprgJVedHSHI9w3n.qrt3NF1rmuX7P9KhI7vZO');
       (4, 'user4', '$2a$12$FBEsdOlED31rlKaac/AvjuDtdu72MdZp4ZjET7SvdidInXxK/9Qya');

CREATE TABLE roles
(
    id              INT NOT NULL    AUTO_INCREMENT,
    name            VARCHAR(45)     NOT NULL UNIQUE,
    PRIMARY KEY     (id)
) COLLATE utf8_bin;

INSERT INTO roles
VALUES (1, 'CLIENT'),
       (2, 'MANAGER'),
       (3, 'ADMIN'),
       (4, 'DEVELOPER');

CREATE TABLE security_users_roles
(
    user_id         INT NOT NULL,
    role_id         INT NOT NULL,
    PRIMARY KEY     (user_id, role_id)
) COLLATE utf8_bin;

INSERT INTO security_users_roles
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4);

CREATE TABLE app_users
(
    id              INT NOT NULL    AUTO_INCREMENT,
    first_name      VARCHAR(45)     NOT NULL UNIQUE,
    last_name       VARCHAR(45)     NOT NULL UNIQUE,
    time_insert     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY     (id)
) COLLATE utf8_bin;

INSERT INTO app_users (id, first_name, last_name)
VALUES (1, 'name1', 'surname1'),
       (2, 'name2', 'surname2'),
       (3, 'name3', 'surname3'),
       (4, 'name4', 'surname4');