package com.ecommerceproject.productservice.controlleradvice;

import com.ecommerceproject.productservice.dtos.ExceptionDto;
import com.ecommerceproject.productservice.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        int status = HttpStatus.BAD_REQUEST.value();
        ExceptionDto dto = createExceptionDto(status, "Malformed JSON request");
        return ResponseEntity
                .status(status)
                .body(dto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        int status = HttpStatus.BAD_REQUEST.value();
        ExceptionDto dto = createExceptionDto(status, "Invalid Parameters");
        return ResponseEntity
                .status(status)
                .body(dto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> handleConstraintViolationException(ConstraintViolationException e) {
        int status = HttpStatus.BAD_REQUEST.value();
        ExceptionDto dto = createExceptionDto(status, "Invalid Request Parameters");
        return ResponseEntity
                .status(status)
                .body(dto);
    }

    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<ExceptionDto> handleInvalidSortFieldException(InvalidSortFieldException e) {
        int status = HttpStatus.BAD_REQUEST.value();
        ExceptionDto dto = createExceptionDto(status, e.getMessage());
        return ResponseEntity
                .status(status)
                .body(dto);
    }

    @ExceptionHandler(InvalidProductRequestException.class)
    public ResponseEntity<ExceptionDto> handleInvalidProductRequestException(InvalidProductRequestException e) {
        int status = HttpStatus.BAD_REQUEST.value();
        ExceptionDto dto = createExceptionDto(status, e.getMessage());
        return ResponseEntity
                .status(status)
                .body(dto);
    }

    @ExceptionHandler(CategoryAlreadyExistException.class)
    public ResponseEntity<ExceptionDto> handleCategoryAlreadyExistExceptionException(CategoryAlreadyExistException e) {
        int status = HttpStatus.BAD_REQUEST.value();
        ExceptionDto dto = createExceptionDto(status, e.getMessage());
        return ResponseEntity
                .status(status)
                .body(dto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleUnexpectedException(Exception e) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        ExceptionDto dto = createExceptionDto(status, "Unexpected error has occurred");
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
