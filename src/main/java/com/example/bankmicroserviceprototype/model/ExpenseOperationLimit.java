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
    private Integer id;

    @Column(name = "total")
    private Float TotalExpensesLimit;

    @Column(name = "products")
    private Float ProductsExpensesLimit;

    @Column(name = "services")
    private Float ServiceExpensesLimit;
    @Column(name = "date_and_time")
    private ZonedDateTime limitSettingDateAndTime;

}
