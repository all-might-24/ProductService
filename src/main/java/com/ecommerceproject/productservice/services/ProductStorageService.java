package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.*;
import com.ecommerceproject.productservice.exceptions.CategoryNotFoundException;
import com.ecommerceproject.productservice.exceptions.ProductNotFoundException;
import com.ecommerceproject.productservice.mapper.ProductMapper;
import com.ecommerceproject.productservice.models.Category;
import com.ecommerceproject.productservice.models.Product;
import com.ecommerceproject.productservice.repositories.CategoryRepository;
import com.ecommerceproject.productservice.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
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
    public CreateProductResponseDto createProduct(CreateProductRequestDto createProductRequestDto) {

        Product product = new Product();
        product.setTitle(createProductRequestDto.getTitle());
        product.setDescription(createProductRequestDto.getDescription());
        product.setPrice(createProductRequestDto.getPrice());
        product.setImageUrl(createProductRequestDto.getImageUrl());
        product.setQty(createProductRequestDto.getQty());

//        Category category = createProductRequestDto.getCategory();
//        Optional<Category> optionalCategory = categoryRepository.findByTitle(category.getTitle());
//
//        if(optionalCategory.isEmpty()) {
//            Category newCategory = new Category();
//            newCategory.setTitle(category.getTitle());
//            newCategory.setDescription(category.getDescription());
//            product.setCategory(categoryRepository.save(newCategory));
//        } else {
//            product.setCategory(optionalCategory.get());
//        }

        Optional<Category> optionalCategory = categoryRepository.findById(createProductRequestDto.getCategoryId());
        if(optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("Category not found with id : " + createProductRequestDto.getCategoryId());
        }
        product.setCategory(optionalCategory.get());

        return productMapper.toCreateProductResponseDto(productRepository.save(product));

    }

    private Product findProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty() || optionalProduct.get().isDeleted()) {
            throw new ProductNotFoundException("Product not found with id : " + id);
        }

        return optionalProduct.get();
    }

    @Override
    public GetProductResponseDto getProductById(Long productId) {

        Product product = findProductById(productId);
        return productMapper.toGetProductResponseDto(product);
    }

    @Override
    public Page<GetProductResponseDto> getAllProducts(String search, Pageable pageable) {
        Page<Product> products;

        if(search != null && !search.isBlank()) {
            products = productRepository
                    .findByIsDeletedFalseAndTitleContainingIgnoreCase(search.trim(), pageable);
        } else {
            products = productRepository
                    .findByIsDeletedFalse(pageable);
        }

        return products.map(productMapper::toGetProductResponseDto);
    }

    @Override
    public void updateProductById(UpdateProductRequestDto productRequestDto, Long productId) {

        Product product = findProductById(productId);

        product.setTitle(productRequestDto.getTitle());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());

        Optional<Category> optionalCategory = categoryRepository.findById(productRequestDto.getCategoryId());
        if(optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("Category not found");
        }
        product.setCategory(optionalCategory.get());

        product.setQty(productRequestDto.getQty());
        product.setImageUrl(productRequestDto.getImageUrl());

        productRepository.save(product);

    }

    @Override
    public void updateProductFieldsById(PatchProductRequestDto productRequestDto, Long productId) {

        Product product = findProductById(productId);

        if(productRequestDto.getTitle() != null) {
            product.setTitle(productRequestDto.getTitle());
        }

        if (productRequestDto.getDescription() != null) {
            product.setDescription(productRequestDto.getDescription());
        }

        if (productRequestDto.getPrice() != null) {
            product.setPrice(productRequestDto.getPrice());
        }

        if (productRequestDto.getCategoryId() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(productRequestDto.getCategoryId());
            if(optionalCategory.isEmpty()) {
                throw new CategoryNotFoundException("Category not found");
            }
            product.setCategory(optionalCategory.get());
        }

        if (productRequestDto.getQty() != null) {
            product.setQty(productRequestDto.getQty());
        }
        if (productRequestDto.getImageUrl() != null) {
            product.setImageUrl(productRequestDto.getImageUrl());
        }
        productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long productId) {

        Product product = findProductById(productId);

        productRepository.delete(product);
    }

    @Override
    public void softDeleteProductById(Long productId) {

        Product product = findProductById(productId);
        product.setDeleted(true);
        productRepository.save(product);
    }
}
