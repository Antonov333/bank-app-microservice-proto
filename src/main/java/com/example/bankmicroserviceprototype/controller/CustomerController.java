package com.example.bankmicroserviceprototype.controller;

import com.example.bankmicroserviceprototype.model.ExpenseLimit;
import com.example.bankmicroserviceprototype.model.ExpenseLimitDto;
import com.example.bankmicroserviceprototype.model.ExpenseOperation;
import com.example.bankmicroserviceprototype.service.ExpenseOperationService;
import com.example.bankmicroserviceprototype.service.LimitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Customer API", description = "Интерфейс для взаимодействия с клиентом")
@RequestMapping("/customer")
public class CustomerController {

    final private LimitService limitService;

    final private ExpenseOperationService operationService;

    @Operation(summary = "Эндпойнт для установки месячного лимита расходных операций",
            description = "По условиям ТЗ валюта лимита USD, дата устанавливается равной текущей, поэтому в эндпойнт мы принимаем только значения лимитов по категориям")
    @PostMapping("/limit/{accountFrom}")
    @ApiResponses(value = {@ApiResponse(responseCode = "503", description = "Service is unavailable until construction completed"),
            @ApiResponse(responseCode = "201", description = "New expense operation limit stored in database")})
    public ResponseEntity<ExpenseLimit> setNewLimit(
            @PathVariable(name = "accountFrom") Long accountFrom,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "В метод эндпойнта необходимо передать DTO месячного лимита расходных операций",
                    content = @Content(schema = @Schema(implementation = ExpenseLimitDto.class))) @RequestBody ExpenseLimitDto expenseLimitDto) {
        return limitService.saveNewLimit(accountFrom, expenseLimitDto);
    }

    @GetMapping("/limit/{accountFrom}")
    @Operation(summary = "Передача клиенту значения текущего лимита")
    public ResponseEntity<ExpenseLimit> getActualLimit(@PathVariable(name = "accountFrom") Long accountFrom) {
        return new ResponseEntity<>(limitService.getActualLimit(accountFrom), HttpStatus.OK);
    }

    @GetMapping("/report/{accountFrom}")
    @Operation(summary = "Передача клиенту списка операций с флагом limit_exceeded =  true за текущий месяц")
    public ResponseEntity<List<ExpenseOperation>> thisMonthExceeded(@PathVariable long accountFrom) {
        return operationService.thisMonthExceeded(accountFrom);
    }

}
