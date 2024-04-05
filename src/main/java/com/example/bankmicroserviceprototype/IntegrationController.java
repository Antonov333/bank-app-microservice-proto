package com.example.bankmicroserviceprototype;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integration")
@Tag(name = "Bank System Integration API", description = "Интерфейс для интеграции с информационной системой банка")
public class IntegrationController {
    @PostMapping()
    public ResponseEntity<HttpStatus> loadExpenseOperation(/*ExpenseOperationDto expenseOperationDto*/){
        return new ResponseEntity<>(HttpStatus.valueOf(777));
    }
}
