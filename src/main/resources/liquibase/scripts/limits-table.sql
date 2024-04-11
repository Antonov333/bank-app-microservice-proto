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

--changeset antonov:11
ALTER table limits DROP COLUMN date_and_time;
ALTER table limits ADD date_and_time TIMESTAMP(0);

--changeset antonov:13
ALTER table limits DROP COLUMN date_and_time;
ALTER table limits ADD date_and_time TIMESTAMP WITH TIME ZONE;