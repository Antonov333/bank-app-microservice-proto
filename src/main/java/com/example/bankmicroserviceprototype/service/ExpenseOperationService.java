package com.example.bankmicroserviceprototype.service;

import com.example.bankmicroserviceprototype.mapper.ModelMapper;
import com.example.bankmicroserviceprototype.model.ExpenseOperation;
import com.example.bankmicroserviceprototype.model.ExpenseOperationDto;
import com.example.bankmicroserviceprototype.repository.ExpenseOperationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExpenseOperationService {

    private final ExpenseOperationRepository expenseOperationRepository;

    private final LimitService limitService;

    private final ExchangeRateService exchangeRateService;

    private final Logger logger = LoggerFactory.getLogger("ExpenseOperationService Logger");


    public ResponseEntity<HttpStatus> saveExpenseOperation(ExpenseOperationDto expenseOperationDto) {

        logger.info("Received Expense Op Dto: " + expenseOperationDto);

        ExpenseOperation expenseOperation = ModelMapper.INSTANCE.toEntity(expenseOperationDto);
        logger.info("Converted to entity: " + expenseOperation);

        // Сохраняем операцию в базе данных
        expenseOperation = expenseOperationRepository.save(expenseOperation);

        /* Определяем, превышен ли лимит расходов */


        float sumThisMonth;
        ZonedDateTime operationDateTime = expenseOperation.getDateTime();
        ZonedDateTime beginningOfThisMonth = beginningOfMonth(operationDateTime);

        List<ExpenseOperation> operationsThisMonth;

        // Получаем операции по аккаунту с начала месяца c учетом категорий текущего лимита
        if (limitsDefinedForAllCategories(expenseOperation.getAccountFrom())) {
            // заданы лимиты по категориям расходов, поэтому ищем операции соответствующей категории,
            // совершенные с начала месяца
            operationsThisMonth = expenseOperationRepository
                    .findByAccountFromAndExpenseCategoryAndDateTimeAfter(
                    expenseOperation.getAccountFrom(),
                    expenseOperation.getExpenseCategory(),
                    beginningOfThisMonth);

            // Считаем сумму
            sumThisMonth = estimateTotalSumOfOperationsInUsd(operationsThisMonth);

            // сравниваем сумму с лимитом и устанавливаем значение флага
            expenseOperation.setLimitExceeded(
                    sumThisMonth >
                            limitService.getActualLimitByCategory(
                                    expenseOperation.getAccountFrom(),
                                    expenseOperation.getExpenseCategory()));

        } else {
            //Не определен лимит по товарам и/или услугам, поэтому применяем лимит на общую сумму расходов
            // и получаем сумму по всем операциям за период
            operationsThisMonth = expenseOperationRepository.findByAccountFromAndDateTimeAfter(
                    expenseOperation.getAccountFrom(),
                    beginningOfThisMonth);
            // Считаем сумму

            sumThisMonth = estimateTotalSumOfOperationsInUsd(operationsThisMonth);


            // сравниваем сумму с лимитом
            expenseOperation.setLimitExceeded(
                    sumThisMonth >
                            limitService.getActualLimit(expenseOperation.getAccountFrom()).getTotalExpensesLimit());
        }

        // сохраняем сущность расходной операции с вычисленным значением флага превышения лимита
        expenseOperation = expenseOperationRepository.save(expenseOperation);
        logger.info("Stored in database: " + expenseOperation);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * @param dateTime some date and time
     * @return date and of 00:00:00 the first day of month specified by argument
     */
    ZonedDateTime beginningOfMonth(ZonedDateTime dateTime) {
        return ZonedDateTime.of(dateTime.getYear(),
                dateTime.getMonthValue(),
                dateTime.getDayOfMonth(), 0, 0, 0, 0, dateTime.getZone());
    }

    float estimateTotalSumOfOperationsInUsd(List<ExpenseOperation> operationList) {
        float sum = 0.0f;
        float exchangeRate;
        for (ExpenseOperation o : operationList) {
            exchangeRate = exchangeRateService.retrieveExchangeRate(o.getCurrencyCode(),
                    o.getDateTime());
            sum += o.getSum();
        }
        return sum;
    }

    boolean limitsDefinedForAllCategories(Long accountFrom) {
        return
                (limitService.getActualLimit(accountFrom).getServiceExpensesLimit() != null)
                        &
                        (limitService.getActualLimit(accountFrom).getServiceExpensesLimit() != null)
                ;
    }

    public ResponseEntity<List<ExpenseOperation>> thisMonthExceeded(long accountFrom) {
        List<ExpenseOperation> operationList = expenseOperationRepository
                .findByAccountFromAndDateTimeAfterAndLimitExceeded(accountFrom,
                        beginningOfMonth(ZonedDateTime.now()), true);
        return new ResponseEntity<>(operationList, HttpStatus.OK);
    }
}
