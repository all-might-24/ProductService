package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.CreateCategoryRequestDto;
import com.ecommerceproject.productservice.dtos.CreateCategoryResponseDto;
import com.ecommerceproject.productservice.exceptions.CategoryAlreadyExistException;
import com.ecommerceproject.productservice.exceptions.CategoryNotFoundException;
import com.ecommerceproject.productservice.mapper.CategoryMapper;
import com.ecommerceproject.productservice.models.Category;
import com.ecommerceproject.productservice.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CreateCategoryResponseDto createCategory(CreateCategoryRequestDto createCategoryRequestDto) {
        Optional<Category> categoryOptional = categoryRepository.findByTitle(createCategoryRequestDto.getTitle());
        if(categoryOptional.isPresent()) {
            throw new CategoryAlreadyExistException("Category Already exists" + createCategoryRequestDto.getTitle());
        }
        Category category = new Category();
        category.setTitle(createCategoryRequestDto.getTitle());
        category.setDescription(createCategoryRequestDto.getDescription());

        return categoryMapper.toCreateCategoryResponseDto(categoryRepository.save(category));
    }

    @Override
    public Category findCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("Category not found with id : " + id);
        }
        return optionalCategory.get();
    }
}
