package com.example.bankmicroserviceprototype.service;

import com.example.bankmicroserviceprototype.mapper.ModelMapper;
import com.example.bankmicroserviceprototype.model.ExchangeRate;
import com.example.bankmicroserviceprototype.model.ExpenseLimit;
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

    /**
     * Метод создает и сохраняет сущность расходной операции. Часть полей заполняется данными из DTO,
     * часть подтягивается через сервисы лимита расходов (expenseLimitId) и обменных курсов(exchangeRateId), поле эквивалентной долларовой суммы
     * вычисляется на основе полученных данных
     *
     * @param expenseOperationDto Данные расходной операции
     * @return код результата выполнения метода
     */
    public ResponseEntity<HttpStatus> saveExpenseOperation(ExpenseOperationDto expenseOperationDto) {

        logger.info("Received Expense Operation DTO: " + expenseOperationDto);

        ExpenseOperation expenseOperation = ModelMapper.INSTANCE.toEntity(expenseOperationDto);
        logger.info("Converted to entity: " + expenseOperation);

        // Сохраняем операцию в базе данных
        expenseOperation = expenseOperationRepository.save(expenseOperation);

        // Получаем обменный курс валюты операции
        ExchangeRate exchangeRate = exchangeRateService.retrieveExchangeRate(
                expenseOperation.getCurrencyCode(),
                expenseOperation.getDateTime());

        // Сохраняем идентификатор сущности обменного курса в поле exchangeRateId
        expenseOperation.setExchangeRateId(exchangeRate.getId());

        // Вычисляем сумму в долларах, эквивалентную сумме операции
        expenseOperation.setEquivalentUsdSum(expenseOperation.getSum() / exchangeRate.getRate());

        /* Определяем, превышен ли лимит расходов */

        ZonedDateTime operationDateTime = expenseOperation.getDateTime();
        ZonedDateTime beginningOfThisMonth = beginningOfMonth(operationDateTime);

        // Получаем операции по аккаунту с начала месяца c учетом категорий текущего лимита
        // Сначала нужно определить, лимит определен по категориям операций,
        // либо на общую сумму расходов, чтобы получить соответствующую выборку операций из БД
        ExpenseLimit expenseLimit = limitService.getActualLimit(expenseOperation.getAccountFrom());
        expenseOperation.setExpenselimitId(expenseLimit.getId());
        if (limitService.limitsDefinedForAllCategories(expenseLimit)) {
            float sumThisMonthOfGivenCategory;
            List<ExpenseOperation> operationsThisMonthOfGivenCategory;
            // заданы лимиты по категориям расходов, поэтому ищем операции соответствующей категории,
            // совершенные с начала месяца
            operationsThisMonthOfGivenCategory = expenseOperationRepository
                    .findByAccountFromAndExpenseCategoryAndDateTimeAfter(
                    expenseOperation.getAccountFrom(),
                    expenseOperation.getExpenseCategory(),
                    beginningOfThisMonth);

            // Считаем сумму
            sumThisMonthOfGivenCategory = calculateTotalSumOfOperationsInUsd(operationsThisMonthOfGivenCategory);

            // сравниваем сумму с лимитом и устанавливаем значение флага
            expenseOperation.setLimitExceeded(
                    sumThisMonthOfGivenCategory >
                            limitService.getCategoryLimitValue(expenseLimit, expenseOperation.getExpenseCategory()));

        } else {
            //Не определен лимит по товарам и/или услугам, поэтому применяем лимит на общую сумму расходов
            // и получаем сумму по всем операциям за период
            List<ExpenseOperation> operationsThisMonthOfCategory = expenseOperationRepository.findByAccountFromAndDateTimeAfter(
                    expenseOperation.getAccountFrom(),
                    beginningOfThisMonth);
            // Считаем сумму

            float sumThisMonth = calculateTotalSumOfOperationsInUsd(operationsThisMonthOfCategory);

            // сравниваем сумму с лимитом
            expenseOperation.setLimitExceeded(
                    sumThisMonth > expenseLimit.getTotalExpensesLimit());
        }

        // сохраняем сущность расходной операции с вычисленным значением флага превышения лимита
        expenseOperation = expenseOperationRepository.save(expenseOperation);
        logger.info("Stored in database: " + expenseOperation);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * @param dateTime some date and time
     * @return date and time of 00:00:00 of the first day of month specified by argument
     */
    ZonedDateTime beginningOfMonth(ZonedDateTime dateTime) {
        return ZonedDateTime.of(dateTime.getYear(),
                dateTime.getMonthValue(),
                dateTime.getDayOfMonth(), 0, 0, 0, 0, dateTime.getZone());
    }

    float calculateTotalSumOfOperationsInUsd(List<ExpenseOperation> operationList) {
        float sum = 0.0f;
        float exchangeRate;
        for (ExpenseOperation o : operationList) {
            sum += o.getEquivalentUsdSum();
        }
        return sum;

        //TODO: complete logic!!

    }

    public ResponseEntity<List<ExpenseOperation>> thisMonthExceeded(long accountFrom) {
        List<ExpenseOperation> operationList = expenseOperationRepository
                .findByAccountFromAndDateTimeAfterAndLimitExceeded(accountFrom,
                        beginningOfMonth(ZonedDateTime.now()), true);
        return new ResponseEntity<>(operationList, HttpStatus.OK);
    }
}
