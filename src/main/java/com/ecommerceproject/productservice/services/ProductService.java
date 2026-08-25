package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.*;
import org.springframework.data.domain.Page;


import org.springframework.data.domain.Pageable;

public interface ProductService {

     GetProductResponseDto getProductById(Long productId);
     Page<GetProductResponseDto> getAllProducts(String search, Pageable pageable);
     CreateProductResponseDto createProduct(CreateProductRequestDto createProductRequestDto);
     void updateProductById(UpdateProductRequestDto product, Long productId);
     void updateProductFieldsById(PatchProductRequestDto product, Long productId);
     void deleteProductById(Long productId);
     void softDeleteProductById(Long productId);
}
