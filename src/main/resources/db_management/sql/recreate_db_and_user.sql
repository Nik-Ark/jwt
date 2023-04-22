DROP USER IF EXISTS 'jwt_user'@'localhost';
DROP DATABASE IF EXISTS jwt_user_db;

CREATE USER IF NOT EXISTS 'jwt_user'@'localhost' IDENTIFIED BY 'jwt_user_pasS:0$';
CREATE DATABASE IF NOT EXISTS jwt_user_db;
GRANT ALL PRIVILEGES ON jwt_user_db.* TO 'jwt_user'@'localhost';
