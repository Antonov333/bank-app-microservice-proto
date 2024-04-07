package com.example.bankmicroserviceprototype.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Model of openexchangerates.org response
 */
@Data
public class ExchangeRateDataDto {
    private String disclaimer;
    private String license;
    private Timestamp timestamp;
    private String base;
    private HashMap<String, Float> rates;
}
