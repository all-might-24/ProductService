package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.CreateCategoryRequestDto;
import com.ecommerceproject.productservice.dtos.CreateCategoryResponseDto;
import com.ecommerceproject.productservice.models.Category;

public interface ICategoryService {
    CreateCategoryResponseDto createCategory(CreateCategoryRequestDto createCategoryRequestDto);

    Category findCategoryById(Long id);
}
