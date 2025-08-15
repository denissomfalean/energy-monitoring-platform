create table if not exists users
(
    id          bigint PRIMARY KEY,
    username    varchar(255),
    password    varchar(255),
    access_role varchar(255)
);