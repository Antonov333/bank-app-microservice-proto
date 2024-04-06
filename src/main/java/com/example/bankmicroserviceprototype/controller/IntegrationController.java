package com.example.bankmicroserviceprototype.controller;

import com.example.bankmicroserviceprototype.model.ExpenseOperationDto;
import com.example.bankmicroserviceprototype.service.ExpenseOperationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/integration")
@Tag(name = "Bank System Integration API", description = "Интерфейс для интеграции с информационной системой банка")
public class IntegrationController {
    final private ExpenseOperationService expenseOperationService;
    @PostMapping()
    public ResponseEntity<HttpStatus> loadExpenseOperation(@RequestBody ExpenseOperationDto expenseOperationDto) {
        return expenseOperationService.saveOp(expenseOperationDto);
    }
}
