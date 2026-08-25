package com.ecommerceproject.productservice.controlleradvice;

import com.ecommerceproject.productservice.dtos.ExceptionDto;
import com.ecommerceproject.productservice.exceptions.CategoryNotFoundException;
import com.ecommerceproject.productservice.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleCategoryNotFoundException(CategoryNotFoundException e) {
        int status = HttpStatus.NOT_FOUND.value();
        ExceptionDto dto = createExceptionDto(status, e.getMessage());

        return ResponseEntity
                .status(status)
                .body(dto);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors =  e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, error ->error.getDefaultMessage()));
        int status = HttpStatus.BAD_REQUEST.value();
        ExceptionDto dto = createExceptionDto(status, "Validation Failed");
        dto.setErrors(errors);
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
