package com.example.bankmicroserviceprototype.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

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
    @Column(name = "accountFrom")
    private long accountFrom;

    /**
     * Банковский счет продавца
     */
    @Column(name = "accountTo")
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
}
