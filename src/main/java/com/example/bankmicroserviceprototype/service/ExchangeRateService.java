package com.example.bankmicroserviceprototype.service;

import com.example.bankmicroserviceprototype.model.ExchangeRateDataDto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * <h2>ExchangeRateService</h2>
 * ExchangeRateService is to get, store and process currency exchange rate
 */
@Service
@Data
public class ExchangeRateService {
    @Value("${my.id}")
    private String apiId;

    public ResponseEntity<ExchangeRateDataDto> readFromOpenExchangeRates() {
        String url = "https://openexchangerates.org/api/latest.json?app_id="
                + apiId;
        // Keep on mind it is possible to put particular currency codes like "&symbols=KZT,RUB"
        // to narrow response
        Mono<ExchangeRateDataDto> exchangeRateDataMono =
                WebClient.create(url).get().retrieve().bodyToMono(ExchangeRateDataDto.class);
        return new ResponseEntity<>(exchangeRateDataMono.share().block(), HttpStatus.OK);
    }
}
