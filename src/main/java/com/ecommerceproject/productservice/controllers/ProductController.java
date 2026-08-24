package com.ecommerceproject.productservice.controllers;

import com.ecommerceproject.productservice.dtos.ProductRequestDto;
import com.ecommerceproject.productservice.dtos.CreateProductResponseDto;
import com.ecommerceproject.productservice.dtos.GetProductResponseDto;
import com.ecommerceproject.productservice.models.Product;
import com.ecommerceproject.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(@Qualifier("productStorageService") ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<CreateProductResponseDto> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        CreateProductResponseDto createProductResponseDto = productService.createProduct(productRequestDto);
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
    public ResponseEntity<List<GetProductResponseDto>> getAllProduct() {
        List<GetProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity
                .ok()
                .body(products);
    }

    @PutMapping("/{product_id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("product_id") Long productId,
                                              @RequestBody ProductRequestDto productRequestDto) {
        productService.updateProductById(productRequestDto, productId);
        return ResponseEntity
                .noContent()
                .build();
    }

}
