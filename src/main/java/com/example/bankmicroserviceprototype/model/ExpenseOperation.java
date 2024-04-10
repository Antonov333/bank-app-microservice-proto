package com.example.bankmicroserviceprototype.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * Сущность расходной операции
 */
@Data
@Entity
@Table(name = "operations")
public class ExpenseOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Банковский счет клиента
     */
    @Column(name = "account_from")
    private long accountFrom;

    /**
     * Банковский счет контрагента
     */
    @Column(name = "account_to")
    private long accountTo;

    /**
     * Сумма операции
     */
    @Column(name = "sum")
    private Float sum;

    /**
     * Код валюты (короткое название)
     */
    @Column(name = "currency")
    private String currencyCode;

    /**
     * Категория расходов
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ExpenseCategory expenseCategory;

    /**
     * Дата и время операции
     */
    @Column(name = "operation_time")
    private ZonedDateTime dateTime;

    /**
     * Флаг превышения лимита расходов
     */
    private boolean limitExceeded;

    /**
     * Идентификатор сущности курса валюты, по которому вычисляется эквивалентная сумма в USD.
     */
    @Column(name = "exchange_rate_id")
    private long exchangeRateId;

    /**
     * Долларовый эквивалент суммы операции
     */
    @Column(name = "sum_usd")
    private float equivalentUsdSum;

    /**
     * Идентификатор лимита расходов, который применяется к операции
     */
    @Column(name = "limit_id")
    private long expenselimitId;
}
