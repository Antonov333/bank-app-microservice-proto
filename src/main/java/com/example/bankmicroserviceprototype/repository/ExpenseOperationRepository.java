package com.example.bankmicroserviceprototype.repository;

import com.example.bankmicroserviceprototype.model.ExpenseCategory;
import com.example.bankmicroserviceprototype.model.ExpenseOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ExpenseOperationRepository extends JpaRepository<ExpenseOperation, Long> {

    List<ExpenseOperation> findByAccountFromAndDateTimeAfter(Long accountFrom, ZonedDateTime dateTime);

    List<ExpenseOperation> findByAccountFromAndExpenseCategoryAndDateTimeAfter(
            Long accountFrom, ExpenseCategory expenseCategory, ZonedDateTime dateTime);
}
