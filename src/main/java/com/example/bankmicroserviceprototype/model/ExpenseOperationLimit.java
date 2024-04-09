package com.example.bankmicroserviceprototype.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "limits")
public class ExpenseOperationLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total")
    private Float totalExpensesLimit;

    @Column(name = "products")
    private Float productsExpensesLimit;

    @Column(name = "services")
    private Float serviceExpensesLimit;

    @Column(name = "date_and_time")
    private ZonedDateTime limitSettingDateAndTime;

    @Column(name = "account_from")
    private Long accountFrom;

}
