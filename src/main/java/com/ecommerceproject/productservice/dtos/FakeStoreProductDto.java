package com.ecommerceproject.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class FakeStoreProductDto {
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private String Category;
    private String imageUrl;

}
//{
//        "id": 0,
//        "title": "string",
//        "price": 0.1,
//        "description": "string",
//        "category": "string",
//        "image": "http://example.com"
//}