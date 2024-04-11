package com.example.bankmicroserviceprototype.controller;

import com.example.bankmicroserviceprototype.model.ExpenseOperationDto;
import com.example.bankmicroserviceprototype.service.ExpenseOperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/integration")
@Tag(name = "Bank System Integration API", description = "Интерфейс для интеграции с информационной системой банка")
public class IntegrationController {
    final private ExpenseOperationService expenseOperationService;
    @PostMapping()
    @Operation(summary = "Эндпойнт для получения данных расходной операции")
    public ResponseEntity<HttpStatus> loadExpenseOperation(@RequestBody ExpenseOperationDto expenseOperationDto) {
        return expenseOperationService.saveExpenseOperation(expenseOperationDto);
    }
}
