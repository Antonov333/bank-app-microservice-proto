package com.example.bankmicroserviceprototype.service;

import com.example.bankmicroserviceprototype.mapper.ModelMapper;
import com.example.bankmicroserviceprototype.model.ExpenseOperationLimit;
import com.example.bankmicroserviceprototype.model.ExpenseOperationLimitDto;
import com.example.bankmicroserviceprototype.repository.LimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Service
public class LimitService {
    private final LimitRepository limitRepository;

    static ExpenseOperationLimit getDefaultLimit() {
        ExpenseOperationLimit limit = new ExpenseOperationLimit();
        limit.setTotalExpensesLimit(1000.00f);
        return limit;
    }

    static String getLimitCurrencyCode() {
        return "USD";
    }

    public ExpenseOperationLimit getActualLimit() {
        ExpenseOperationLimit actualLimit = getDefaultLimit();

        // Look up database. If empty then apply default limit $1000 total,
        // else look through limit database to consider actual limit
        if (limitRepository.count() > 0) {
            return limitRepository.findById(limitRepository.count()).orElse(getDefaultLimit());
        }
        return actualLimit;
    }

    public ResponseEntity<ExpenseOperationLimit> saveNewLimit(ExpenseOperationLimitDto expenseOperationLimitDto) {
        ExpenseOperationLimit expenseOperationLimit = ModelMapper.INSTANCE
                .getLimitEntityFromDto(expenseOperationLimitDto);
        expenseOperationLimit.setLimitSettingDateAndTime(ZonedDateTime.now());
        expenseOperationLimit = limitRepository.save(expenseOperationLimit);
        return new ResponseEntity<>(expenseOperationLimit, HttpStatus.CREATED);
    }
}
