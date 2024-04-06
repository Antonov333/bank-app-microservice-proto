package com.example.bankmicroserviceprototype.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Currency;

/**
 * DTO модели расходной операции
 */
@Data
public class ExpenseOperationDto {

    /**
     * Банковский счет клиента
     */
    private long accountFrom;
    /**
     * Банковский счет продавца
     */
    private long accountTo;
    /**
     * Сумма операции
     */
    private Double sum;

    /**
     * Строка, означающая код валюты
     */
    private String currencyCode;
    private ExpenseCategory expenseCategory;
    private ZonedDateTime dateTime;
}
