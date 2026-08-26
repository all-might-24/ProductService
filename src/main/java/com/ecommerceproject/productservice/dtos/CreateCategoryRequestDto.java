package com.ecommerceproject.productservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

}
