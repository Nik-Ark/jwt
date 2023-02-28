USE jwt_user_db;

DROP TABLE IF EXISTS security_users;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS security_users_authorities;

CREATE TABLE security_users
(
    id              INT NOT NULL    AUTO_INCREMENT,
    username        VARCHAR(45)     NOT NULL UNIQUE,
    password        VARCHAR(100)    NOT NULL,
    time_insert     TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY     (id)
) COLLATE utf8_bin;

INSERT INTO security_users (id, username, password)
VALUES (1, 'user1', '$2a$12$f66aaKBc6f23La7JaN5ca.obWwmx8ENNHG.pDNdgHTAkgxVEIOqgW'),
       (2, 'user2', '$2a$12$eV8SV478lxghvjeLDNbH.eUyd4AW.81SFTKjcViyJAOO0qkaJbiG2'),
       (3, 'user3', '$2y$10$/DGK1xfFrrnHaKswprgJVedHSHI9w3n.qrt3NF1rmuX7P9KhI7vZO');

CREATE TABLE authorities
(
    id              INT NOT NULL    AUTO_INCREMENT,
    name            VARCHAR(45)     NOT NULL,
    PRIMARY KEY     (id)
) COLLATE utf8_bin;

INSERT INTO authorities
VALUES (1, 'read'),
       (2, 'write'),
       (3, 'update'),
       (4, 'delete');

CREATE TABLE security_users_authorities
(
    user_id         INT NOT NULL,
    authority_id    INT NOT NULL,
    PRIMARY KEY     (user_id, authority_id)
) COLLATE utf8_bin;

INSERT INTO security_users_authorities
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 1),
       (2, 2),
       (3, 1);