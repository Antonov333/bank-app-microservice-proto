package com.example.bankmicroserviceprototype.controller;

import com.example.bankmicroserviceprototype.model.ExpenseOperationLimit;
import com.example.bankmicroserviceprototype.model.ExpenseOperationLimitDto;
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

@RequiredArgsConstructor
@RestController
@Tag(name = "Customer API", description = "Интерфейс для взаимодействия с клиентом")
@RequestMapping("/customer")
public class CustomerController {

    final private LimitService limitService;

    @Operation(summary = "Эндпойнт для установки месячного лимита расходных операций",
            description = "По условиям ТЗ валюта лимита USD, дата устанавливается равной текущей, поэтому в эндпойнт мы принимаем только значения лимитов по категориям")
    @PostMapping("/limit/{accountFrom}")
    @ApiResponses(value = {@ApiResponse(responseCode = "503", description = "Service is unavailable until construction completed"),
            @ApiResponse(responseCode = "201", description = "New expense operation limit stored in database")})
    public ResponseEntity<ExpenseOperationLimit> setNewLimit(
            @PathVariable(name = "accountFrom") Long accountFrom,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "В метод эндпойнта необходимо передать DTO месячного лимита расходных операций",
            content = @Content(schema = @Schema(implementation = ExpenseOperationLimitDto.class))) @RequestBody ExpenseOperationLimitDto expenseLimitDto) {
        return limitService.saveNewLimit(accountFrom, expenseLimitDto);
    }

    @GetMapping("/limit/{accountFrom}")
    @Operation(summary = "Передача клиенту значения текущего лимита")
    public ResponseEntity<ExpenseOperationLimit> getActualLimit(@PathVariable(name = "accountFrom") Long accountFrom) {
        return new ResponseEntity<>(limitService.getActualLimit(accountFrom), HttpStatus.OK);
    }


}
