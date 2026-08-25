package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements ProductService{

    private final RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GetProductResponseDto getProductById(Long productId) {
        ResponseEntity<FakeStoreProductDto> response = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/" + productId,
                FakeStoreProductDto.class
        );

        FakeStoreProductDto fakeStoreProductDto = response.getBody();
        return from(fakeStoreProductDto);
    }

    @Override
    public Page<GetProductResponseDto> getAllProducts(String search, Pageable pageable) {
        return null;
    }

    public List<GetProductResponseDto> getAllProducts() {

        ResponseEntity<FakeStoreProductDto[]> responseEntity = restTemplate.getForEntity(
                "https://fakestoreapi.com/products",
                FakeStoreProductDto[].class
        );

        List<GetProductResponseDto> products = new ArrayList<>();
        for(FakeStoreProductDto productDto : responseEntity.getBody()){
            products.add(from(productDto));
        }
        return products;
    }

    @Override
    public CreateProductResponseDto createProduct(CreateProductRequestDto createProductRequestDto) {
        return null;
    }

    @Override
    public void updateProductById(UpdateProductRequestDto product, Long productId) {
        return ;
    }

    @Override
    public void updateProductFieldsById(PatchProductRequestDto product, Long productId) {

    }

    @Override
    public void deleteProductById(Long productId) {

    }

    @Override
    public void softDeleteProductById(Long productId) {

    }

    private GetProductResponseDto from(FakeStoreProductDto fakeStoreProductDto) {
        if(fakeStoreProductDto != null) {
            GetProductResponseDto product = new GetProductResponseDto();
            product.setTitle(fakeStoreProductDto.getTitle());
            product.setPrice(fakeStoreProductDto.getPrice());
            product.setDescription(fakeStoreProductDto.getDescription());
            //product.setQty(FakeStoreProductDto.);
            product.setImageUrl(fakeStoreProductDto.getImageUrl());
            product.setCategoryName(fakeStoreProductDto.getCategory());
            return product;
        }
        return null;
    }
}
