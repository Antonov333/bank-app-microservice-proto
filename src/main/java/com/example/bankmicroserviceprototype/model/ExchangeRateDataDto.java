package com.example.bankmicroserviceprototype.model;

import lombok.Data;

import java.util.HashMap;

/**
 * Model of openexchangerates.org response
 */
@Data
public class ExchangeRateDataDto {
    private String disclaimer;
    private String license;
    private Long timestamp;
    private String base;
    private HashMap<String, Float> rates;
}
