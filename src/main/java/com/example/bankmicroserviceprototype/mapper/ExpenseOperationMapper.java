package com.example.bankmicroserviceprototype.mapper;

import com.example.bankmicroserviceprototype.model.ExpenseOperation;
import com.example.bankmicroserviceprototype.model.ExpenseOperationDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExpenseOperationMapper {

    ExpenseOperationMapper INSTANCE = Mappers.getMapper(ExpenseOperationMapper.class);

    public ExpenseOperation toEntity(ExpenseOperationDto expenseOperationDto);

}
