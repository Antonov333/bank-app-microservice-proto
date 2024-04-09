package com.example.bankmicroserviceprototype.service;

import com.example.bankmicroserviceprototype.model.ExchangeRate;
import com.example.bankmicroserviceprototype.model.ExchangeRateDataDto;
import com.example.bankmicroserviceprototype.repository.ExchangeRateRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

/**
 * <h2>ExchangeRateService</h2>
 * ExchangeRateService is to get, store and process currency exchange rate
 */
@Service
@Data
@RequiredArgsConstructor
public class ExchangeRateService {
    @Value("${my.id}")
    private String apiId;

    final private ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateDataDto getDtoFromOpenExchangeRates() {
        String url = "https://openexchangerates.org/api/latest.json?app_id="
                + apiId;
        // Keep on mind it is possible to put particular currency codes like "&symbols=KZT,RUB"
        // to narrow response
        Mono<ExchangeRateDataDto> exchangeRateDataMono =
                WebClient.create(url).get().retrieve().bodyToMono(ExchangeRateDataDto.class);
        return exchangeRateDataMono.share().block();
    }

    public void saveExchangeRates(ExchangeRateDataDto exchangeRateDataDto) {
        if (exchangeRateDataDto == null) {
            return;
        }
        Map<String, Float> ratesMap = exchangeRateDataDto.getRates();
        if (ratesMap == null) {
            return;
        }
        Set<String> availableCurrencies = exchangeRateDataDto.getRates().keySet();
        String baseCurrency = exchangeRateDataDto.getBase();
        ZonedDateTime dateAndTime = ZonedDateTime.now();
        availableCurrencies.forEach((a) -> saveRate(dateAndTime, a, ratesMap.get(a), baseCurrency));

    }

    public void saveRate(ZonedDateTime dateAndTime, String currencyCode, Float rateValue, String baseCurrency) {
        ExchangeRate newExchangeRate = new ExchangeRate();
        newExchangeRate.setDateTime(dateAndTime);
        newExchangeRate.setCurrencyCode(currencyCode);
        newExchangeRate.setRate(rateValue);
        newExchangeRate.setBase(baseCurrency);

        exchangeRateRepository.save(newExchangeRate);
    }

    public ResponseEntity<ExchangeRateDataDto> readFromOpenExchangeRates() {
        return new ResponseEntity<>(getDtoFromOpenExchangeRates(), HttpStatus.OK);
    }
}
