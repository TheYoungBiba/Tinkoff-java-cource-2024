--liquibase formatted sql
--changeset TheYoungBiba:1
CREATE TABLE users(
    id BIGINT NOT NULL,
    first_name TEXT NOT NULL,
    PRIMARY KEY (id)
);
