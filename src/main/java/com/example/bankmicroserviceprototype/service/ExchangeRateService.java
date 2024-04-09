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

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
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

    public ExchangeRateDataDto getDtoFromOpenExchangeRatesHistorical(ZonedDateTime dateTime) {

        // Need rates of today, so just retrieve latest
        if (dateTime.toLocalDate().equals(LocalDate.now())) {
            return getDtoFromOpenExchangeRates();
        }

        String url = "https://openexchangerates.org/api/historical/" + dateTime.toLocalDate().toString() + ".json?app_id="
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

    public ResponseEntity<ExchangeRateDataDto> getCurrencyRatesFromOpenExchangeRates() {
        ExchangeRateDataDto exchangeRateDataDto = getDtoFromOpenExchangeRates();
        saveExchangeRates(exchangeRateDataDto);
        return new ResponseEntity<>(exchangeRateDataDto, HttpStatus.OK);
    }

    public List<ExchangeRate> getCurrencyRatesFromDataBase(String currencyCode) {
        return exchangeRateRepository.findByCurrencyCode(currencyCode);
    }

    /**
     * Получаем курс валюты операции к USD на дату операции
     * Сначала ищем по своей базе данных, если нужный курс отсутствует, обращаемся к внешнему сайту. Полученные данные сохраняем в базе.
     *
     * @param currencyCode трехбуквенное обозначение валюты
     * @param dateTime     дата
     * @return
     */

    public float retrieveExchangeRate(String currencyCode, ZonedDateTime dateTime) {
        // Requesting database for required exchange rates
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findByCurrencyAndDateTime(
                currencyCode, dateTime);
        if (exchangeRates.isEmpty()) {
            ExchangeRateDataDto exchangeRateDataDto = getDtoFromOpenExchangeRatesHistorical(dateTime);
            saveExchangeRates(exchangeRateDataDto);
            return exchangeRateDataDto.getRates().get(currencyCode);
        }
        exchangeRates = exchangeRates.stream().sorted(Comparator.comparing(ExchangeRate::getDateTime)).toList();
        return exchangeRates.get(exchangeRates.size() - 1).getRate();
    }
}
