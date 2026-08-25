package com.ecommerceproject.productservice.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateProductRequestDto {

    @NotBlank
    private String title;

    @Positive
    @NotNull
    private BigDecimal price;

    @NotBlank
    private String description;

    @Size(max = 500)
    private String imageUrl;

    @NotNull
    private Long categoryId;

    @NotNull
    @PositiveOrZero
    private Integer qty;

}
