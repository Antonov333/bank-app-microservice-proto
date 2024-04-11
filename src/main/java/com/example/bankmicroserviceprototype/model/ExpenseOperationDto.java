package com.example.bankmicroserviceprototype.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * DTO модели расходной операции
 */
@Data
@Schema(description = "DTO модели расходной операции")
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
