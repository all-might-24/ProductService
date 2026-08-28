package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.CreateCategoryRequestDto;
import com.ecommerceproject.productservice.dtos.CreateCategoryResponseDto;
import com.ecommerceproject.productservice.exceptions.CategoryAlreadyExistException;
import com.ecommerceproject.productservice.exceptions.CategoryNotFoundException;
import com.ecommerceproject.productservice.mapper.CategoryMapper;
import com.ecommerceproject.productservice.models.Category;
import com.ecommerceproject.productservice.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    void createCategory_shouldCreateCategorySuccessfully() {

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();

        requestDto.setTitle("Electronics");
        requestDto.setDescription("Electronic products");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setTitle("Electronics");
        savedCategory.setDescription("Electronic products");

        CreateCategoryResponseDto expectedResponse = new CreateCategoryResponseDto();

        when(categoryRepository.findByTitle("Electronics"))
                .thenReturn(Optional.empty());

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(savedCategory);

        when(categoryMapper.toCreateCategoryResponseDto(savedCategory))
                .thenReturn(expectedResponse);

        CreateCategoryResponseDto actualResponse =
                categoryService.createCategory(requestDto);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        verify(categoryRepository).findByTitle("Electronics");

        verify(categoryRepository).save(any(Category.class));

        verify(categoryMapper).toCreateCategoryResponseDto(savedCategory);
    }


    @Test
    void createCategory_shouldThrowException_whenCategoryAlreadyExists() {

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();

        requestDto.setTitle("Electronics");
        requestDto.setDescription("Electronic products");

        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setTitle("Electronics");

        when(categoryRepository.findByTitle("Electronics"))
                .thenReturn(Optional.of(existingCategory));

        assertThrows(CategoryAlreadyExistException.class, () -> categoryService.createCategory(requestDto)
        );

        verify(categoryRepository).findByTitle("Electronics");

        verify(categoryRepository, never()).save(any(Category.class));

        verify(categoryMapper, never()).toCreateCategoryResponseDto(any(Category.class));
    }


    @Test
    void findCategoryById_shouldReturnCategory_whenCategoryExists() {

        Category category = new Category();
        category.setId(1L);
        category.setTitle("Electronics");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        Category actualCategory = categoryService.findCategoryById(1L);

        assertNotNull(actualCategory);
        assertEquals(category, actualCategory);

        verify(categoryRepository).findById(1L);
    }


    @Test
    void findCategoryById_shouldThrowException_whenCategoryDoesNotExist() {

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findCategoryById(999L)
        );

        verify(categoryRepository).findById(999L);
    }
}