package com.ecommerceproject.productservice.dtos;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GetProductResponseDto {
    private String title;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private String categoryName;
    private Integer qty;

}
