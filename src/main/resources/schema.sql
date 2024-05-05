

CREATE SCHEMA IF NOT EXISTS securecapita;

SET NAMES 'UTF8MB4';
--SET TIME_ZONE='US/Eastern';
--set TIME_ZONE='-4:00';

USE securecapita;

DROP TABLE IF EXISTS Users;

CREATE TABLE Users
(
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    email varchar(100) not null,
    password varchar(255) default null,
    address varchar(255) default null,
    phone varchar(255) default null,
    title varchar(50) default null,
    bio varchar (255) default null,
    non_locked BOOLEAN DEFAULT FALSE,
    using_mfa BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    image_url varchar(255) DEFAULT NULL,
    CONSTRAINT UQ_Users_Email UNIQUE (email)


);


DROP TABLE IF EXISTS Roles;

CREATE TABLE Roles
(
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    permissions  VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_Users_Name UNIQUE (name)


);


DROP TABLE IF EXISTS UserRoles;

CREATE TABLE UserRoles
(
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    role_id BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES Roles (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_UserRoles_User_Id UNIQUE (user_id)


);

DROP TABLE IF EXISTS Events;

CREATE TABLE Events
(
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL CHECK(type IN ('LOGIN_ATTEMPT','LOGIN_ATTEMPT_FAILURE','PROFILE_UPDATE','PROFILE_PICTURE_UPDATE','ROLE_UPDATE','ACCOUNT_SETTING_UPDATE','PASSWORD_UPDATE','MFA_UPDATE')),
    description  VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_Events_Type UNIQUE (type)


);


DROP TABLE IF EXISTS UserEvents;

CREATE TABLE UserEvents
(
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    event_id BIGINT UNSIGNED NOT NULL,
    device varchar(200) DEFAULT NULL,
    ip_address VARCHAR(100) DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (event_id) REFERENCES Events (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

DROP TABLE IF EXISTS AccountVerification;

CREATE TABLE AccountVerification
(
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    url VARCHAR(255) NOT NULL,
    --date DATETIME DEFAULT CURRENT_TIMESTAMP,
    --created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT UQ_AccountVerification_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_AccountVerification_Url UNIQUE (url)
);

DROP TABLE IF EXISTS ResetPasswordVerification;

CREATE TABLE ResetPasswordVerification
(
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    url VARCHAR(255) NOT NULL,
    expiration_date DATETIME NOT NULL,
   -- created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT UQ_ResetPasswordVerification_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_ResetPasswordVerification_Url UNIQUE (url)
);


DROP TABLE IF EXISTS TwoFactorVerification;

CREATE TABLE TwoFactorVerification
(
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    code VARCHAR(10) NOT NULL,
    expiration_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT UQ_TwoFactorVerification_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_TwoFactorVerification_Code UNIQUE (code)
);