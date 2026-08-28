package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.CreateProductRequestDto;
import com.ecommerceproject.productservice.dtos.CreateProductResponseDto;
import com.ecommerceproject.productservice.dtos.GetProductResponseDto;
import com.ecommerceproject.productservice.dtos.PageResponseDto;
import com.ecommerceproject.productservice.dtos.PatchProductRequestDto;
import com.ecommerceproject.productservice.dtos.UpdateProductRequestDto;
import com.ecommerceproject.productservice.exceptions.InvalidSortFieldException;
import com.ecommerceproject.productservice.exceptions.ProductNotFoundException;
import com.ecommerceproject.productservice.mapper.ProductMapper;
import com.ecommerceproject.productservice.models.Category;
import com.ecommerceproject.productservice.models.Product;
import com.ecommerceproject.productservice.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ProductStorageServiceTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductStorageService productService;


// =========================================================
// CREATE
// =========================================================

    @Test
    void postProduct_shouldCreateProductSuccessfully() {

        CreateProductRequestDto requestDto = new CreateProductRequestDto();

        requestDto.setTitle("Test Title");
        requestDto.setDescription("This is a mock test");
        requestDto.setQty(1);
        requestDto.setPrice(new BigDecimal("100.00"));
        requestDto.setImageUrl("testurl.com");
        requestDto.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setTitle("mock category");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setTitle("Test Title");

        CreateProductResponseDto expectedResponse = new CreateProductResponseDto();

        when(categoryService.findCategoryById(1L))
                .thenReturn(category);

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        when(productMapper.toCreateProductResponseDto(savedProduct))
                .thenReturn(expectedResponse);

        CreateProductResponseDto actualResponse = productService.createProduct(requestDto);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        verify(categoryService).findCategoryById(1L);

        verify(productRepository).save(any(Product.class));

        verify(productMapper).toCreateProductResponseDto(savedProduct);
    }


// =========================================================
// GET BY ID
// =========================================================

    @Test
    void getProductById_shouldReturnProduct_whenProductExists() {

        Product product = new Product();
        product.setId(1L);
        product.setTitle("Gaming Laptop");

        GetProductResponseDto expectedResponse = new GetProductResponseDto();

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productMapper.toGetProductResponseDto(product))
                .thenReturn(expectedResponse);

        GetProductResponseDto actualResponse = productService.getProductById(1L);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        verify(productRepository).findById(1L);

        verify(productMapper).toGetProductResponseDto(product);
    }


    @Test
    void getProductById_shouldThrowException_whenProductDoesNotExist() {

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));

        verify(productRepository).findById(999L);

        verify(productMapper, never()).toGetProductResponseDto(any(Product.class));
    }


    @Test
    void getProductById_shouldThrowException_whenProductIsDeleted() {

        Product product = new Product();
        product.setId(1L);
        product.setTitle("Deleted Product");
        product.setDeleted(true);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));

        verify(productRepository).findById(1L);

        verify(productMapper, never()).toGetProductResponseDto(any(Product.class));
    }


// =========================================================
// UPDATE
// =========================================================

    @Test
    void updateProductById_shouldUpdateProductSuccessfully() {

        Long productId = 1L;

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setTitle("Old Title");

        Category category = new Category();
        category.setId(2L);
        category.setTitle("Electronics");

        UpdateProductRequestDto requestDto = new UpdateProductRequestDto();

        requestDto.setTitle("New Title");
        requestDto.setDescription("Updated description");
        requestDto.setPrice(new BigDecimal("500.00"));
        requestDto.setQty(10);
        requestDto.setImageUrl("newimage.com");
        requestDto.setCategoryId(2L);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));

        when(categoryService.findCategoryById(2L))
                .thenReturn(category);

        productService.updateProductById(requestDto, productId);

        assertEquals("New Title", existingProduct.getTitle());
        assertEquals("Updated description", existingProduct.getDescription());
        assertEquals(new BigDecimal("500.00"), existingProduct.getPrice());
        assertEquals(10, existingProduct.getQty());
        assertEquals("newimage.com", existingProduct.getImageUrl());
        assertEquals(category, existingProduct.getCategory());

        verify(productRepository).findById(productId);

        verify(categoryService).findCategoryById(2L);

        verify(productRepository).save(existingProduct);
    }


    @Test
    void updateProductById_shouldThrowException_whenProductDoesNotExist() {

        Long productId = 999L;

        UpdateProductRequestDto requestDto = new UpdateProductRequestDto();

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProductById(requestDto, productId));

        verify(productRepository).findById(productId);

        verify(productRepository, never()).save(any(Product.class));
    }


// =========================================================
// PATCH
// =========================================================

    @Test
    void patchProduct_shouldUpdateOnlyProvidedFields() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setTitle("Old Title");
        product.setDescription("Old Description");
        product.setPrice(new BigDecimal("100.00"));
        product.setQty(5);
        product.setImageUrl("oldimage.com");

        PatchProductRequestDto requestDto = new PatchProductRequestDto();

        requestDto.setTitle("New Title");
        requestDto.setPrice(new BigDecimal("200.00"));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        productService.updateProductFieldsById(requestDto, productId);

        assertEquals("New Title", product.getTitle());
        assertEquals(new BigDecimal("200.00"), product.getPrice());

        // These should remain unchanged
        assertEquals("Old Description", product.getDescription());

        assertEquals(5, product.getQty());
        assertEquals("oldimage.com", product.getImageUrl());

        verify(productRepository).save(product);
    }


    @Test
    void patchProduct_shouldThrowException_whenProductDoesNotExist() {

        Long productId = 999L;

        PatchProductRequestDto requestDto = new PatchProductRequestDto();

        requestDto.setTitle("New Title");

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProductFieldsById(requestDto, productId));

        verify(productRepository, never()).save(any(Product.class));
    }


// =========================================================
// DELETE
// =========================================================

    @Test
    void deleteProductById_shouldDeleteProductSuccessfully() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setTitle("Product");

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        productService.deleteProductById(productId);

        verify(productRepository).findById(productId);

        verify(productRepository).delete(product);
    }


    @Test
    void deleteProductById_shouldThrowException_whenProductDoesNotExist() {

        Long productId = 999L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProductById(productId));

        verify(productRepository, never()).delete(any(Product.class));
    }


// =========================================================
// SOFT DELETE
// =========================================================

    @Test
    void softDeleteProductById_shouldMarkProductAsDeleted() {

        Long productId = 1L;

        Product product = new Product();
        product.setId(productId);
        product.setDeleted(false);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        productService.softDeleteProductById(productId);

        assertTrue(product.isDeleted());

        verify(productRepository).findById(productId);

        verify(productRepository).save(product);
    }


    @Test
    void softDeleteProductById_shouldThrowException_whenProductDoesNotExist() {

        Long productId = 999L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.softDeleteProductById(productId));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getAllProducts_shouldReturnProducts_whenNoFiltersProvided() {

        Pageable pageable = PageRequest.of(0, 5);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setTitle("Laptop");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setTitle("Phone");

        Page<Product> productPage =
                new PageImpl<>(
                        List.of(product1, product2),
                        pageable,
                        2
                );

        GetProductResponseDto dto1 =
                new GetProductResponseDto();

        GetProductResponseDto dto2 =
                new GetProductResponseDto();

        when(productRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(productPage);

        when(productMapper.toGetProductResponseDto(product1))
                .thenReturn(dto1);

        when(productMapper.toGetProductResponseDto(product2))
                .thenReturn(dto2);

        PageResponseDto<GetProductResponseDto> response =
                productService.getAllProducts(
                        null,
                        null,
                        null,
                        null,
                        pageable
                );

        assertNotNull(response);

        assertEquals(
                2,
                response.getContent().size()
        );

        verify(productRepository)
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );

        verify(productMapper)
                .toGetProductResponseDto(product1);

        verify(productMapper)
                .toGetProductResponseDto(product2);
    }


    @Test
    void getAllProducts_shouldApplySearchFilter() {

        Pageable pageable = PageRequest.of(0, 5);

        Product product = new Product();

        product.setId(1L);
        product.setTitle("Gaming Laptop");

        Page<Product> productPage =
                new PageImpl<>(
                        List.of(product),
                        pageable,
                        1
                );

        GetProductResponseDto dto =
                new GetProductResponseDto();

        when(productRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(productPage);

        when(productMapper.toGetProductResponseDto(product))
                .thenReturn(dto);

        PageResponseDto<GetProductResponseDto> response =
                productService.getAllProducts(
                        "Gaming",
                        null,
                        null,
                        null,
                        pageable
                );

        assertNotNull(response);

        assertEquals(
                1,
                response.getContent().size()
        );

        verify(productRepository)
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );
    }


    @Test
    void getAllProducts_shouldApplyCategoryFilter() {

        Pageable pageable = PageRequest.of(0, 5);

        Product product = new Product();

        product.setId(1L);
        product.setTitle("Gaming Laptop");

        Page<Product> productPage =
                new PageImpl<>(
                        List.of(product),
                        pageable,
                        1
                );

        GetProductResponseDto dto =
                new GetProductResponseDto();

        when(productRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(productPage);

        when(productMapper.toGetProductResponseDto(product))
                .thenReturn(dto);

        PageResponseDto<GetProductResponseDto> response =
                productService.getAllProducts(
                        null,
                        10L,
                        null,
                        null,
                        pageable
                );

        assertNotNull(response);

        assertEquals(
                1,
                response.getContent().size()
        );

        verify(productRepository)
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );
    }


    @Test
    void getAllProducts_shouldApplyMinimumPriceFilter() {

        Pageable pageable = PageRequest.of(0, 5);

        Product product = new Product();

        product.setId(1L);
        product.setPrice(
                new BigDecimal("500.00")
        );

        Page<Product> productPage =
                new PageImpl<>(
                        List.of(product),
                        pageable,
                        1
                );

        GetProductResponseDto dto =
                new GetProductResponseDto();

        when(productRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(productPage);

        when(productMapper.toGetProductResponseDto(product))
                .thenReturn(dto);

        PageResponseDto<GetProductResponseDto> response =
                productService.getAllProducts(
                        null,
                        null,
                        new BigDecimal("500.00"),
                        null,
                        pageable
                );

        assertNotNull(response);

        assertEquals(
                1,
                response.getContent().size()
        );

        verify(productRepository)
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );
    }


    @Test
    void getAllProducts_shouldApplyMaximumPriceFilter() {

        Pageable pageable = PageRequest.of(0, 5);

        Product product = new Product();

        product.setId(1L);
        product.setPrice(
                new BigDecimal("1000.00")
        );

        Page<Product> productPage =
                new PageImpl<>(
                        List.of(product),
                        pageable,
                        1
                );

        GetProductResponseDto dto =
                new GetProductResponseDto();

        when(productRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(productPage);

        when(productMapper.toGetProductResponseDto(product))
                .thenReturn(dto);

        PageResponseDto<GetProductResponseDto> response =
                productService.getAllProducts(
                        null,
                        null,
                        null,
                        new BigDecimal("1000.00"),
                        pageable
                );

        assertNotNull(response);

        assertEquals(
                1,
                response.getContent().size()
        );

        verify(productRepository)
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );
    }


    @Test
    void getAllProducts_shouldApplyCombinedFilters() {

        Pageable pageable = PageRequest.of(0, 5);

        Product product = new Product();

        product.setId(1L);
        product.setTitle("Gaming Laptop");

        Page<Product> productPage =
                new PageImpl<>(
                        List.of(product),
                        pageable,
                        1
                );

        GetProductResponseDto dto =
                new GetProductResponseDto();

        when(productRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(productPage);

        when(productMapper.toGetProductResponseDto(product))
                .thenReturn(dto);

        PageResponseDto<GetProductResponseDto> response =
                productService.getAllProducts(
                        "Gaming",
                        1L,
                        new BigDecimal("500.00"),
                        new BigDecimal("2000.00"),
                        pageable
                );

        assertNotNull(response);

        assertEquals(
                1,
                response.getContent().size()
        );

        verify(productRepository)
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );
    }


    @Test
    void getAllProducts_shouldAllowValidSortField() {

        Pageable pageable =
                PageRequest.of(
                        0,
                        5,
                        Sort.by("title")
                );

        Page<Product> productPage =
                new PageImpl<>(
                        List.of(),
                        pageable,
                        0
                );

        when(productRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(productPage);

        PageResponseDto<GetProductResponseDto> response =
                productService.getAllProducts(
                        null,
                        null,
                        null,
                        null,
                        pageable
                );

        assertNotNull(response);

        verify(productRepository)
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );
    }


    @Test
    void getAllProducts_shouldThrowException_whenInvalidSortFieldProvided() {

        Pageable pageable =
                PageRequest.of(
                        0,
                        5,
                        Sort.by("isDeleted")
                );

        assertThrows(
                InvalidSortFieldException.class,
                () -> productService.getAllProducts(
                        null,
                        null,
                        null,
                        null,
                        pageable
                )
        );

        verify(productRepository, never())
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );
    }


    @Test
    void getAllProducts_shouldHandleSearchWithWhitespace() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> productPage =
                new PageImpl<>(
                        List.of(),
                        pageable,
                        0
                );

        when(productRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(productPage);

        PageResponseDto<GetProductResponseDto> response =
                productService.getAllProducts(
                        "   Laptop   ",
                        null,
                        null,
                        null,
                        pageable
                );

        assertNotNull(response);

        verify(productRepository)
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );
    }


    @Test
    void getAllProducts_shouldIgnoreBlankSearch() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> productPage =
                new PageImpl<>(
                        List.of(),
                        pageable,
                        0
                );

        when(productRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(productPage);

        PageResponseDto<GetProductResponseDto> response =
                productService.getAllProducts(
                        "   ",
                        null,
                        null,
                        null,
                        pageable
                );

        assertNotNull(response);

        verify(productRepository)
                .findAll(
                        any(Specification.class),
                        eq(pageable)
                );
    }
}
