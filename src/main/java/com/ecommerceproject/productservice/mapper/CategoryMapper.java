package com.ecommerceproject.productservice.mapper;

import com.ecommerceproject.productservice.dtos.CreateCategoryResponseDto;
import com.ecommerceproject.productservice.models.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CreateCategoryResponseDto toCreateCategoryResponseDto(Category category) {
        CreateCategoryResponseDto createCategoryResponseDto = new CreateCategoryResponseDto();
        createCategoryResponseDto.setId(category.getId());
        createCategoryResponseDto.setTitle(category.getTitle());
        createCategoryResponseDto.setCreatedAt(category.getCreatedAt());

        return createCategoryResponseDto;

    }
}
