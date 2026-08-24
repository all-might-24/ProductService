package com.ecommerceproject.productservice.dtos;

import com.ecommerceproject.productservice.models.Category;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequestDto {
    private String productName;
    private BigDecimal price;
    private String description;
    private Category category;
    private String imageUrl;
    private Integer qty;
}
