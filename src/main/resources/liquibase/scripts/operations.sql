-- liquibase formatted sql
-- changeset antonov:1
CREATE table operations (
                            id BIGSERIAL PRIMARY KEY,
                            accountFrom INT8,
                            accountTo INT8,
                            sum FLOAT, /** Сумма операции */
                            currency CHAR(3), /** Код валюты (короткое название) */
                            category VARCHAR, /** Категория расходов */
                            operation_time TIMESTAMP /** Дата и время операции */)