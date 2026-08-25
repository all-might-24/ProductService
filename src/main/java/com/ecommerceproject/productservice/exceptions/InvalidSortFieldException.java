package com.ecommerceproject.productservice.exceptions;

public class InvalidSortFieldException extends RuntimeException{
    public InvalidSortFieldException(String message) {
        super(message);
    }
}
