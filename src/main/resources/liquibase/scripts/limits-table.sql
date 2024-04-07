-- liquibase formatted sql
-- changeset antonov:2

CREATE table limits(
                        id SERIAL PRIMARY KEY,
                        total FLOAT,
                        products FLOAT,
                        services FLOAT,
                        date_and_time TIMESTAMP
);