package com.ecommerceproject.productservice.exceptions;

public class InvalidProductRequestException extends  RuntimeException{
    public InvalidProductRequestException(String message) {
        super(message);
    }
}
