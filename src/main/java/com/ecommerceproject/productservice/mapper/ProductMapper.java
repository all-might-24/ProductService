package com.ecommerceproject.productservice.mapper;

import com.ecommerceproject.productservice.dtos.CreateProductResponseDto;
import com.ecommerceproject.productservice.dtos.GetProductResponseDto;
import com.ecommerceproject.productservice.models.Product;
import org.springframework.stereotype.Component;


@Component
public class ProductMapper {

    public CreateProductResponseDto toCreateProductResponseDto(Product product) {
        CreateProductResponseDto createProductResponseDto = new CreateProductResponseDto();
        createProductResponseDto.setId(product.getId());
        createProductResponseDto.setTitle(product.getTitle());
        return createProductResponseDto;
    }

    public GetProductResponseDto toGetProductResponseDto(Product product) {
        GetProductResponseDto getProductResponseDto = new GetProductResponseDto();

        getProductResponseDto.setId(product.getId());
        getProductResponseDto.setTitle(product.getTitle());
        getProductResponseDto.setPrice(product.getPrice());
        getProductResponseDto.setDescription(product.getDescription());
        getProductResponseDto.setImageUrl(product.getImageUrl());
        getProductResponseDto.setQty(product.getQty());

        getProductResponseDto.setCategoryName(product.getCategory().getTitle());

        return getProductResponseDto;
    }

}
