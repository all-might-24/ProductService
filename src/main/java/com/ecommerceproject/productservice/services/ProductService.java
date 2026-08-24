package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.ProductRequestDto;
import com.ecommerceproject.productservice.dtos.CreateProductResponseDto;
import com.ecommerceproject.productservice.dtos.GetProductResponseDto;
import com.ecommerceproject.productservice.models.Product;

import java.util.List;

public interface ProductService {

     GetProductResponseDto getProductById(Long productId);
     List<GetProductResponseDto> getAllProducts();
     CreateProductResponseDto createProduct(ProductRequestDto productRequestDto);
     void updateProductById(ProductRequestDto product, Long productId);
     void deleteProductById(Long productId);
}
