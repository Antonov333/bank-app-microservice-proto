package com.example.bankmicroserviceprototype.service;

import com.example.bankmicroserviceprototype.model.ExpenseOperationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.http.HttpResponse;

@Service
public class ExpenseOperationService {
    public ResponseEntity<HttpStatus> saveOp(ExpenseOperationDto expenseOperationDto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
