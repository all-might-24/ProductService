package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.FakeStoreProductDto;
import com.ecommerceproject.productservice.models.Category;
import com.ecommerceproject.productservice.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements ProductService{

    private final RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(Long productId) {
        ResponseEntity<FakeStoreProductDto> response = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/" + productId,
                FakeStoreProductDto.class
        );

        FakeStoreProductDto fakeStoreProductDto = response.getBody();
        return from(fakeStoreProductDto);
    }

    @Override
    public List<Product> getAllProducts() {

        ResponseEntity<FakeStoreProductDto[]> responseEntity = restTemplate.getForEntity(
                "https://fakestoreapi.com/products",
                FakeStoreProductDto[].class
        );

        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDto productDto : responseEntity.getBody()){
            products.add(from(productDto));
        }
        return products;
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

    private Product from(FakeStoreProductDto fakeStoreProductDto) {
        if(fakeStoreProductDto != null) {
            Product product = new Product();
            product.setId(fakeStoreProductDto.getId());
            product.setTitle(fakeStoreProductDto.getTitle());
            product.setPrice(fakeStoreProductDto.getPrice());
            product.setDescription(fakeStoreProductDto.getDescription());
            //product.setQty(FakeStoreProductDto.);
            product.setImageUrl(fakeStoreProductDto.getImageUrl());

            Category category = new Category();
            category.setTitle(fakeStoreProductDto.getCategory());
            product.setCategory(category);

            return product;
        }
        return null;
    }
}
