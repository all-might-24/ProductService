package com.ecommerceproject.productservice.controlleradvice;

import com.ecommerceproject.productservice.dtos.ExceptionDto;
import com.ecommerceproject.productservice.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleProductNotFoundException(ProductNotFoundException e) {
        int status = HttpStatus.NOT_FOUND.value();
        ExceptionDto dto = createExceptionDto(status, e.getMessage());

        return ResponseEntity
                .status(status)
                .body(dto);

    }

    private ExceptionDto createExceptionDto(int status, String message) {
        ExceptionDto dto = new ExceptionDto();

        dto.setStatus(status);
        dto.setMessage(message);
        dto.setTimeStamp(LocalDateTime.now());

        return dto;
    }

}
