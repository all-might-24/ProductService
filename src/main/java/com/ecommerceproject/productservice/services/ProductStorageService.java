package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.ProductRequestDto;
import com.ecommerceproject.productservice.dtos.CreateProductResponseDto;
import com.ecommerceproject.productservice.dtos.GetProductResponseDto;
import com.ecommerceproject.productservice.exceptions.ProductNotFoundException;
import com.ecommerceproject.productservice.mapper.ProductMapper;
import com.ecommerceproject.productservice.models.Category;
import com.ecommerceproject.productservice.models.Product;
import com.ecommerceproject.productservice.repositories.CategoryRepository;
import com.ecommerceproject.productservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductStorageService implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;


    public ProductStorageService(ProductRepository productRepository,
                                 ProductMapper productMapper,
                                 CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CreateProductResponseDto createProduct(ProductRequestDto productRequestDto) {

        Product product = new Product();
        product.setTitle(productRequestDto.getProductName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setImageUrl(productRequestDto.getImageUrl());
        product.setQty(productRequestDto.getQty());

        Category category = productRequestDto.getCategory();
        Optional<Category> optionalCategory = categoryRepository.findByTitle(category.getTitle());

        if(optionalCategory.isEmpty()) {
            Category newCategory = new Category();
            newCategory.setTitle(category.getTitle());
            newCategory.setDescription(category.getDescription());
            product.setCategory(categoryRepository.save(newCategory));
        } else {
            product.setCategory(optionalCategory.get());
        }

        return productMapper.toCreateProductResponseDto(productRepository.save(product));

    }

    @Override
    public GetProductResponseDto getProductById(Long productId) {
        Optional<Product> optional = productRepository.findById(productId);

        if(optional.isEmpty()) {
            throw new ProductNotFoundException("Product Not Found");
        }
        Product product = optional.get();
        return productMapper.toGetProductResponseDto(product);
    }

    @Override
    public List<GetProductResponseDto> getAllProducts() {

        List<Product> products = productRepository.findAll();

        return products
                .stream()
                .map(productMapper::toGetProductResponseDto)
                .toList();
    }

    @Override
    public void updateProductById(ProductRequestDto productRequestDto, Long productId) {

        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        Product product = optionalProduct.get();
        product.setTitle(productRequestDto.getProductName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());

        Category category = product.getCategory();
        category.setTitle(productRequestDto.getCategory().getTitle());
        category.setDescription(productRequestDto.getCategory().getDescription());

        product.setQty(productRequestDto.getQty());
        product.setImageUrl(productRequestDto.getImageUrl());

        productRepository.save(product);

    }

    @Override
    public void deleteProductById(Long productId) {

    }
}
