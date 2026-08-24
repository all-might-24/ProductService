package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.models.Product;

import java.util.List;

public interface ProductService {

     Product getProductById(Long productId);
     List<Product> getAllProducts();
     Product createProduct(Product product);
     Product updateProductById(Product product, Long productId);
     void deleteProductById(Long productId);
}
