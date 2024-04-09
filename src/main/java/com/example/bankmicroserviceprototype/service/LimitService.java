package com.example.bankmicroserviceprototype.service;

import com.example.bankmicroserviceprototype.mapper.ModelMapper;
import com.example.bankmicroserviceprototype.model.ExpenseCategory;
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

    /**
     * Метод проверяет наличие лимитов в базе данных и при отсутствии записей в таблице лимитов возвращает лимит по умолчанию.
     * При наличии записей возвращается последний установленный лимит.
     *
     * @return действующий лимит суммы расходных операций
     */

    public ExpenseOperationLimit getActualLimit() {
        ExpenseOperationLimit actualLimit = getDefaultLimit();

        // Look up database. If empty then apply default limit $1000 total,
        // else look through limit database and apply latest
        if (limitRepository.count() > 0) {
            return limitRepository.findById(limitRepository.count()).orElse(getDefaultLimit());
        }
        return actualLimit;
    }

    Float getActualLimitByCategory(ExpenseCategory expenseCategory) {
        if (ExpenseCategory.SERVICE.equals(expenseCategory)) {
            return getActualLimit().getServiceExpensesLimit();
        }
        if (ExpenseCategory.PRODUCT.equals(expenseCategory)) {
            return getActualLimit().getProductsExpensesLimit();
        }
        return getActualLimit().getTotalExpensesLimit();
    }

    public ResponseEntity<ExpenseOperationLimit> saveNewLimit(ExpenseOperationLimitDto expenseOperationLimitDto) {

        ExpenseOperationLimit expenseOperationLimit = ModelMapper.INSTANCE
                .getLimitEntityFromDto(expenseOperationLimitDto);

        expenseOperationLimit.setLimitSettingDateAndTime(ZonedDateTime.now());

        expenseOperationLimit = limitRepository.save(expenseOperationLimit);

        return new ResponseEntity<>(expenseOperationLimit, HttpStatus.CREATED);
    }
}
