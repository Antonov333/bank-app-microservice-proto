package com.example.bankmicroserviceprototype.repository;

import com.example.bankmicroserviceprototype.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    List<ExchangeRate> findByCurrencyCode(String currencyCode);
}
