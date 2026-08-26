package com.ecommerceproject.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateCategoryResponseDto {
    Long id;
    String title;
    LocalDateTime createdAt;
}
