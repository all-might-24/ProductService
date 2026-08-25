package com.ecommerceproject.productservice.validators;

import com.ecommerceproject.productservice.exceptions.InvalidProductRequestException;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public class ProductRequestValidator {

    public static void validate(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {

        if(minPrice != null && minPrice.signum() < 0) {
            throw new InvalidProductRequestException("Minimum price cannot be negative");
        }

        if(maxPrice != null && maxPrice.signum() < 0) {
            throw new InvalidProductRequestException("Maximum price cannot be negative");
        }

        if(minPrice != null && maxPrice != null && maxPrice.compareTo(minPrice) < 0) {
            throw new InvalidProductRequestException("Minimum price should not be greater than Maximum price");
        }

        if (pageable.getPageSize() > 100) {
            throw new InvalidProductRequestException("Page size cannot exceed 100");
        }
    }
}
