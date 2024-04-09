package com.example.bankmicroserviceprototype.repository;

import com.example.bankmicroserviceprototype.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    List<ExchangeRate> findByCurrencyCode(String currencyCode);

    @Query(nativeQuery = true,
            value = "SELECT * FROM exchange_rates e WHERE currency_code=?1 and date_time LIKE % ?2")
    List<ExchangeRate> findByCurrencyAndDateTime(String currencyCode, ZonedDateTime dateTime);
}
