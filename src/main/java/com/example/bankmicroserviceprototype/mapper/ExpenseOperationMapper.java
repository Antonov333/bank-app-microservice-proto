package com.example.bankmicroserviceprototype.mapper;

import com.example.bankmicroserviceprototype.model.ExpenseOperation;
import com.example.bankmicroserviceprototype.model.ExpenseOperationDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Мапперы для преобразования DTO в сущность, сущность в DTO и т.п.
 */
@Mapper
public interface ExpenseOperationMapper {

    ExpenseOperationMapper INSTANCE = Mappers.getMapper(ExpenseOperationMapper.class);

    /**
     * Маппер для получения сущности расходной операции на основе значений полей DTO
     *
     * @param expenseOperationDto DTO расходной операции
     * @return сущность расходной операции
     */
    ExpenseOperation toEntity(ExpenseOperationDto expenseOperationDto);

}
