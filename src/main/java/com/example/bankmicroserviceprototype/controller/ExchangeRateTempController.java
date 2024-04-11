package com.example.bankmicroserviceprototype.controller;

import com.example.bankmicroserviceprototype.model.ExchangeRate;
import com.example.bankmicroserviceprototype.model.ExchangeRateDataDto;
import com.example.bankmicroserviceprototype.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/temp")
@Tag(name = "Exchange Rate Controller", description = "Контроллер для проверки функционала сервиса курсов валют в ходе разработки")
public class ExchangeRateTempController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping()
    public String getApiId() {
        return exchangeRateService.getApiId();
    }

    @GetMapping("/read-currency-rates-from-openexchangerates")
    public ResponseEntity<ExchangeRateDataDto> readFromWeb() {
        return exchangeRateService.getCurrencyRatesFromOpenExchangeRates();
    }

    @GetMapping("/find-currency-rates-in-db/{currency}")
    public ResponseEntity<List<ExchangeRate>> findRatesInDatabase(@PathVariable String currency) {
        return new ResponseEntity<>(exchangeRateService.getCurrencyRatesFromDataBase(currency), HttpStatus.OK);
    }

}
