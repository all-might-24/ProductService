package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.*;

import java.util.List;

public interface ProductService {

     GetProductResponseDto getProductById(Long productId);
     List<GetProductResponseDto> getAllProducts();
     CreateProductResponseDto createProduct(CreateProductRequestDto createProductRequestDto);
     void updateProductById(UpdateProductRequestDto product, Long productId);
     void updateProductFieldsById(PatchProductRequestDto product, Long productId);
     void deleteProductById(Long productId);
     void softDeleteProductById(Long productId);
}
