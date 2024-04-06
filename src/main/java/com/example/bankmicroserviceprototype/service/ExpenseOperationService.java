package com.example.bankmicroserviceprototype.service;

import com.example.bankmicroserviceprototype.mapper.ExpenseOperationMapper;
import com.example.bankmicroserviceprototype.model.ExpenseOperation;
import com.example.bankmicroserviceprototype.model.ExpenseOperationDto;
import com.example.bankmicroserviceprototype.repository.ExpenseOperationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExpenseOperationService {
    private final ExpenseOperationRepository expenseOperationRepository;
    private final Logger logger = LoggerFactory.getLogger("ExpenseOperationService Logger");
    public ResponseEntity<HttpStatus> saveOp(ExpenseOperationDto expenseOperationDto) {
        logger.info("Received Expense Op Dto: " + expenseOperationDto);
        ExpenseOperation expenseOperation = ExpenseOperationMapper.INSTANCE.toEntity(expenseOperationDto);
        logger.info("Converted to entity: " + expenseOperation);
        expenseOperation = expenseOperationRepository.save(expenseOperation);
        logger.info("Stored in database: " + expenseOperation);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
