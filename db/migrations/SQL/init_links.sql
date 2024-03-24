--liquibase formatted sql
--changeset TheYoungBiba:1
CREATE TABLE links(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    url TEXT NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    checked_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (url)
);
