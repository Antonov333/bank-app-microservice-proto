package com.example.bankmicroserviceprototype.service;

import com.example.bankmicroserviceprototype.mapper.ModelMapper;
import com.example.bankmicroserviceprototype.model.ExpenseCategory;
import com.example.bankmicroserviceprototype.model.ExpenseLimit;
import com.example.bankmicroserviceprototype.model.ExpenseLimitDto;
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

    static ExpenseLimit getDefaultLimit() {
        ExpenseLimit limit = new ExpenseLimit();
        limit.setTotalExpensesLimit(1000.00f);
        limit.setId(0L);
        return limit;
    }

    static String getLimitCurrencyCode() {
        return "USD";
    }

    /**
     * Метод проверяет наличие лимитов в базе данных и при отсутствии записей в таблице лимитов возвращает лимит по умолчанию.
     * При наличии записей возвращается последний установленный лимит.
     * @param accountFrom банковский счет клиента
     * @return сущность действующего лимита расходных операций
     */
    public ExpenseLimit getActualLimit(long accountFrom) {
        ExpenseLimit actualLimit = getDefaultLimit();
        actualLimit.setAccountFrom(accountFrom);
        // Look up database. If empty then apply default limit $1000 total,
        // else look through limit database and apply latest
        if (!limitRepository.findByAccountFrom(accountFrom).isEmpty()) {
            List<ExpenseLimit> limitList = limitRepository.findByAccountFrom(accountFrom)
                    .stream().sorted(Comparator.comparing(ExpenseLimit::getLimitSettingDateAndTime))
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

    public ResponseEntity<ExpenseLimit> saveNewLimit(
            Long accountFrom,
            ExpenseLimitDto expenseLimitDto) {

        ExpenseLimit expenseOperationLimit = ModelMapper.INSTANCE
                .getLimitEntityFromDto(expenseLimitDto);

        expenseOperationLimit.setLimitSettingDateAndTime(ZonedDateTime.now());
        expenseOperationLimit.setAccountFrom(accountFrom);

        expenseOperationLimit = limitRepository.save(expenseOperationLimit);

        return new ResponseEntity<>(expenseOperationLimit, HttpStatus.CREATED);
    }

    public float getCategoryLimitValue(ExpenseLimit expenseLimit, ExpenseCategory expenseCategory) {
        if (!limitsDefinedForAllCategories(expenseLimit)) {
            return 0.0F;
        }
        switch (expenseCategory) {
            case SERVICE:
                return expenseLimit.getProductsExpensesLimit();
            case PRODUCT:
                return expenseLimit.getServiceExpensesLimit();
        }
        return 0.0F;
    }

    public boolean limitsDefinedForAllCategories(ExpenseLimit expenseLimit) {
        if (expenseLimit == null) {
            return false;
        }
        return (expenseLimit.getServiceExpensesLimit() != null) &
                (expenseLimit.getProductsExpensesLimit() != null);
    }
}
