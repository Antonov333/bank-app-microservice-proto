package com.example.bankmicroserviceprototype.controller;

import com.example.bankmicroserviceprototype.model.ExpenseOperationDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integration")
@Tag(name = "Bank System Integration API", description = "Интерфейс для интеграции с информационной системой банка")
public class IntegrationController {
    @PostMapping()
    public ResponseEntity<HttpStatus> loadExpenseOperation(ExpenseOperationDto expenseOperationDto) {
        return new ResponseEntity<>(HttpStatus.valueOf(777));
    }
}
