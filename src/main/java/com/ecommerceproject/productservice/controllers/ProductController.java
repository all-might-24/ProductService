package com.ecommerceproject.productservice.controllers;

import com.ecommerceproject.productservice.dtos.*;
import com.ecommerceproject.productservice.services.ProductService;
import com.ecommerceproject.productservice.validators.ProductRequestValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(@Qualifier("productStorageService") ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<CreateProductResponseDto> createProduct( @Valid @RequestBody CreateProductRequestDto createProductRequestDto) {
        CreateProductResponseDto createProductResponseDto = productService.createProduct(createProductRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createProductResponseDto);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<GetProductResponseDto> getProductById(@PathVariable("product_id") Long productId) {
        GetProductResponseDto getProductResponseDto =  productService.getProductById(productId);

        return ResponseEntity
                .ok()
                .body(getProductResponseDto);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<GetProductResponseDto>> getAllProduct(@RequestParam(required = false) String search,
                                                                     @RequestParam(required = false) Long categoryId,
                                                                     @RequestParam(required = false) BigDecimal minPrice,
                                                                     @RequestParam(required = false) BigDecimal maxPrice,
                                                                     Pageable pageable) {
        ProductRequestValidator.validate(minPrice, maxPrice, pageable);

        PageResponseDto<GetProductResponseDto> products = productService.getAllProducts(search, categoryId, minPrice, maxPrice, pageable);
        return ResponseEntity
                .ok()
                .body(products);
    }

    @PutMapping("/{product_id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("product_id") Long productId,
                                              @Valid @RequestBody UpdateProductRequestDto productRequestDto) {
        productService.updateProductById(productRequestDto, productId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/{product_id}")
    public ResponseEntity<Void> updateProductFields(@PathVariable("product_id") Long productId,
                                                    @Valid @RequestBody PatchProductRequestDto productRequestDto) {
        productService.updateProductFieldsById(productRequestDto, productId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{product_id}/hard-delete")
    public ResponseEntity<Void> deleteProduct(@PathVariable("product_id") Long productId) {
        productService.deleteProductById(productId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<Void> softDeleteProduct(@PathVariable("product_id") Long productId) {
        productService.softDeleteProductById(productId);

        return ResponseEntity
                .noContent()
                .build();
    }

}
