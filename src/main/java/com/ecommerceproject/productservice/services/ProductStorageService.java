package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.models.Product;
import com.ecommerceproject.productservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductStorageService implements ProductService{

    private ProductRepository productRepository;

    public ProductStorageService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getProductById(Long productId) {
        Optional<Product> optional = productRepository.findById(productId);

        if(optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public Product updateProductById(Product product, Long productId) {
        return null;
    }

    @Override
    public void deleteProductById(Long productId) {

    }
}
