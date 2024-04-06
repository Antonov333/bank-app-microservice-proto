-- liquibase formatted sql
-- changeset antonov:1
CREATE table operations (
                            id BIGSERIAL PRIMARY KEY,
                            account_from INT8,
                            account_to INT8,
                            sum FLOAT, /** Сумма операции */
                            currency CHAR(3), /** Код валюты (короткое название) */
                            category VARCHAR, /** Категория расходов */
                            operation_time TIMESTAMP /** Дата и время операции */,
                            limit_exceeded boolean /** Флаг превышения лимита*/);