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
import java.util.Comparator;
import java.util.List;

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

    public ExpenseOperationLimit getActualLimit(long accountFrom) {
        ExpenseOperationLimit actualLimit = getDefaultLimit();
        actualLimit.setAccountFrom(accountFrom);

        // Look up database. If empty then apply default limit $1000 total,
        // else look through limit database and apply latest
        if (!limitRepository.findByAccountFrom(accountFrom).isEmpty()) {
            List<ExpenseOperationLimit> limitList = limitRepository.findByAccountFrom(accountFrom)
                    .stream().sorted(Comparator.comparing(ExpenseOperationLimit::getLimitSettingDateAndTime))
                    .toList();
            actualLimit = limitList.get(limitList.size() - 1);

        }
        return actualLimit;
    }

    Float getActualLimitByCategory(long accountFrom, ExpenseCategory expenseCategory) {
        if (ExpenseCategory.SERVICE.equals(expenseCategory)) {
            return getActualLimit(accountFrom).getServiceExpensesLimit();
        }
        if (ExpenseCategory.PRODUCT.equals(expenseCategory)) {
            return getActualLimit(accountFrom).getProductsExpensesLimit();
        }
        return getActualLimit(accountFrom).getTotalExpensesLimit();
    }

    public ResponseEntity<ExpenseOperationLimit> saveNewLimit(
            Long accountFrom,
            ExpenseOperationLimitDto expenseOperationLimitDto) {

        ExpenseOperationLimit expenseOperationLimit = ModelMapper.INSTANCE
                .getLimitEntityFromDto(expenseOperationLimitDto);

        expenseOperationLimit.setLimitSettingDateAndTime(ZonedDateTime.now());
        expenseOperationLimit.setAccountFrom(accountFrom);

        expenseOperationLimit = limitRepository.save(expenseOperationLimit);

        return new ResponseEntity<>(expenseOperationLimit, HttpStatus.CREATED);
    }
}
