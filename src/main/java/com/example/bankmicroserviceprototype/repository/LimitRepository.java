package com.example.bankmicroserviceprototype.repository;

import com.example.bankmicroserviceprototype.model.ExpenseLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimitRepository extends JpaRepository<ExpenseLimit, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM limits WHERE date_and_time < today")
    List<ExpenseLimit> findLimitsEstablishedBeforeToday();

    List<ExpenseLimit> findByAccountFrom(Long accountFrom);
}
