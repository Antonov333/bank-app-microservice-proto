-- liquibase formatted sql
-- changeset antonov:4

CREATE table exchange_rates(
                                id BIGSERIAL PRIMARY KEY,
                                date_time TIMESTAMP,
                                currency_code CHAR(3),
                                base CHAR(3),
                                rate FLOAT);
-- changeset antonov:5
ALTER table exchange_rates DROP COLUMN base;
ALTER table exchange_rates ADD base VARCHAR;

-- changeset antonov:6
ALTER table exchange_rates DROP COLUMN currency_code;
ALTER table exchange_rates ADD currency_code VARCHAR;

--changeset antonov:10
ALTER table exchange_rates DROP COLUMN date_time;
ALTER table exchange_rates ADD date_time TIMESTAMP(0);

--changeset antonov:12
ALTER table exchange_rates DROP COLUMN date_time;
ALTER table exchange_rates ADD date_time TIMESTAMP WITH TIME ZONE;