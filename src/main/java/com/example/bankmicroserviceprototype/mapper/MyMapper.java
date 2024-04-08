package com.example.bankmicroserviceprototype.mapper;

import com.example.bankmicroserviceprototype.model.ExpenseOperation;
import com.example.bankmicroserviceprototype.model.ExpenseOperationDto;
import com.example.bankmicroserviceprototype.model.ExpenseOperationLimit;
import com.example.bankmicroserviceprototype.model.ExpenseOperationLimitDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Мапперы для преобразования DTO в сущность, сущность в DTO и т.п.
 */
@Mapper
public interface MyMapper {

    MyMapper INSTANCE = Mappers.getMapper(MyMapper.class);

    /**
     * Маппер для получения сущности расходной операции на основе значений полей DTO
     *
     * @param expenseOperationDto DTO расходной операции
     * @return сущность расходной операции
     */
    ExpenseOperation toEntity(ExpenseOperationDto expenseOperationDto);

    ExpenseOperationLimit getLimitEntityFromDto(ExpenseOperationLimitDto expenseOperationLimitDto);

}
