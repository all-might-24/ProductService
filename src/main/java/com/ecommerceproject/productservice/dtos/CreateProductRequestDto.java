package com.ecommerceproject.productservice.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequestDto {

    @NotBlank
    private String title;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotBlank
    private String description;

    @NotNull
    private Long categoryId;

    @Size(max = 500)
    private String imageUrl;

    @PositiveOrZero
    private Integer qty;
}
