-- liquibase formatted sql
-- changeset antonov:2

CREATE table limits(
                        id SERIAL PRIMARY KEY,
                        total FLOAT,
                        products FLOAT,
                        services FLOAT,
                        date_and_time TIMESTAMP
);

-- changeset antonov:3
DROP table limits;
CREATE table limits(
                        id BIGSERIAL PRIMARY KEY,
                        total FLOAT,
                        products FLOAT,
                        services FLOAT,
                        date_and_time TIMESTAMP
);

-- changeset antonov:7
ALTER table limits ADD account_from int8;