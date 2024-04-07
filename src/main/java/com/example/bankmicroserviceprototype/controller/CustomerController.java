package com.example.bankmicroserviceprototype.controller;

import com.example.bankmicroserviceprototype.model.ExpenseOperationLimitDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Customer API", description = "Интерфейс для взаимодействия с клиентом")
@RequestMapping("/customer")
public class CustomerController {
    @Operation(summary = "Эндпойнт для установки месяного лимита расходных операций",
            description = "По условиям ТЗ валюта лимита USD, дата устанавливается равной текущей, поэтому в эндпойнт мы принимаем только значения лимитов по категориям")
    @PostMapping("/limit")
    @ApiResponses(@ApiResponse(responseCode = "503", description = "Service is unavailable until construction completed"))
    public ResponseEntity<HttpStatus> setNewLimit(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "В метод эндпойнта необходимо передать DTO месячного лимита расходных операций",
            content = @Content(schema = @Schema(implementation = ExpenseOperationLimitDto.class))) @RequestBody ExpenseOperationLimitDto expenseLimitDto) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/limit")
    @Operation(summary = "Передача клиенту значения текущего лимита")
    public ResponseEntity<ExpenseOperationLimitDto> getActualLimit() {
        return new ResponseEntity<ExpenseOperationLimitDto>(new ExpenseOperationLimitDto(), HttpStatus.NOT_IMPLEMENTED);
    }


}
