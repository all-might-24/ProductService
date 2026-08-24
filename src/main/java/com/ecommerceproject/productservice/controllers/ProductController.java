package com.ecommerceproject.productservice.controllers;

import com.ecommerceproject.productservice.models.Product;
import com.ecommerceproject.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(@Qualifier("productStorageService") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World!";
    }

    @GetMapping("/{product_id}")
    public Product getProductById(@PathVariable("product_id") Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping
    public List<Product> getAllProduct() {
        List<Product> products = productService.getAllProducts();
        return products;
    }


}
