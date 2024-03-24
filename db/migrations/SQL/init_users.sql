--liquibase formatted sql
--changeset TheYoungBiba:1
CREATE TABLE users(
    id BIGINT NOT NULL,
    PRIMARY KEY (id)
);
