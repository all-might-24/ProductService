package com.ecommerceproject.productservice.controllers;

import com.ecommerceproject.productservice.dtos.CreateCategoryRequestDto;
import com.ecommerceproject.productservice.dtos.CreateCategoryResponseDto;
import com.ecommerceproject.productservice.services.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CreateCategoryResponseDto> createCategory(@Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
        CreateCategoryResponseDto createCategoryResponseDto = categoryService.createCategory(createCategoryRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createCategoryResponseDto);
    }
}
