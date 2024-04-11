package com.example.bankmicroserviceprototype.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * Сущность обменного курса валюты
 */
@Data
@Entity
@Table(name = "exchange_rates")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time", columnDefinition = "TIME WITH TIME ZONE")
    private ZonedDateTime dateTime;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "base")
    private String base;

    @Column(name = "rate")
    private Float rate;
}
