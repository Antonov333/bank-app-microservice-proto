package com.example.bankmicroserviceprototype.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Дата установки лимита автоматически устанавливается текущая, валюта лимитов: USD")
public class ExpenseOperationLimitDto {

    @Schema(description = "Общий лимит. Игнорируется, если указаны все лимиты по категориям")
    private Float TotalExpensesLimit;

    @Schema(description = "Лимит расходов на товары")
    private Float ProductsExpensesLimit;

    @Schema(description = "Лимит расходов на услуги")
    private Float ServiceExpensesLimit;

}
