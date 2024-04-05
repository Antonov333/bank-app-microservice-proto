package com.example.bankmicroserviceprototype;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Customer API", description = "Интерфейс для получения запросов клиента")
@RequestMapping("/customer")
public class CustomerController {
    @PostMapping("/limit")
    public ResponseEntity<HttpStatus> setNewLimit(/*ExpenseLimitDto expenseLimitDto*/) {
        return new ResponseEntity<>(HttpStatus.valueOf(777));
    }
}
