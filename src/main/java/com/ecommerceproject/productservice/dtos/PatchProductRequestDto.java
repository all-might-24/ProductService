package com.ecommerceproject.productservice.dtos;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PatchProductRequestDto {

    private String title;

    @Positive
    private BigDecimal price;

    private String description;

    @Size(max = 500)
    private String imageUrl;

    private Long categoryId;

    @PositiveOrZero
    private Integer qty;

}
