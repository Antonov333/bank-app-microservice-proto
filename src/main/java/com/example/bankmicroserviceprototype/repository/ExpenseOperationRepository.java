package com.example.bankmicroserviceprototype.repository;

import com.example.bankmicroserviceprototype.model.ExpenseOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseOperationRepository extends JpaRepository<ExpenseOperation, Long> {
}
