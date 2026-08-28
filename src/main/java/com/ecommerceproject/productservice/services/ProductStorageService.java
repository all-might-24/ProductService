package com.ecommerceproject.productservice.services;

import com.ecommerceproject.productservice.dtos.*;
import com.ecommerceproject.productservice.exceptions.CategoryNotFoundException;
import com.ecommerceproject.productservice.exceptions.InvalidSortFieldException;
import com.ecommerceproject.productservice.exceptions.ProductNotFoundException;
import com.ecommerceproject.productservice.mapper.ProductMapper;
import com.ecommerceproject.productservice.models.Category;
import com.ecommerceproject.productservice.models.Product;
import com.ecommerceproject.productservice.repositories.CategoryRepository;
import com.ecommerceproject.productservice.repositories.ProductRepository;
import com.ecommerceproject.productservice.specifications.ProductSpecifications;
import com.ecommerceproject.productservice.utilities.GlobalVariables;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.ecommerceproject.productservice.utilities.GlobalVariables.VALID_PRODUCT_SORT_FIELDS;

@Service
public class ProductStorageService implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;


    public ProductStorageService(ProductRepository productRepository,
                                 ProductMapper productMapper,
                                 CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryService = categoryService;
    }

    @Override
    @Transactional
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

        Category category = categoryService.findCategoryById(createProductRequestDto.getCategoryId());

        product.setCategory(category);

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
    public PageResponseDto<GetProductResponseDto> getAllProducts(String search,
                                                      Long categoryId,
                                                      BigDecimal minPrice,
                                                      BigDecimal maxPrice,
                                                      Pageable pageable) {
        validateSortingFields(pageable);

        Specification<Product> specification = ProductSpecifications.isNotDeleted();

        if(search != null && !search.isBlank()) {
            specification = specification.and(ProductSpecifications.containsTitle(search.trim()));
        }

        if (categoryId != null) {
            specification = specification.and(ProductSpecifications.hasCategoryId(categoryId));
        }

        if(minPrice != null) {
            specification = specification.and(ProductSpecifications.priceGreaterThanOrEqualTo(minPrice));
        }

        if(maxPrice != null) {
            specification = specification.and(ProductSpecifications.priceLesserThanOrEqualTo(maxPrice));
        }
        Page<Product> products = productRepository.findAll(specification, pageable);

        Page<GetProductResponseDto> productsDto = products.map(productMapper::toGetProductResponseDto);

        return PageResponseDto.from(productsDto);

    }

    private void validateSortingFields(Pageable pageable) {
        pageable.getSort().forEach( order -> {
            if (!GlobalVariables.VALID_PRODUCT_SORT_FIELDS.contains(order.getProperty())) {
                throw new InvalidSortFieldException("Sorting by "+ order.getProperty() +" is not allowed");
            }
        });
    }

    @Override
    @Transactional
    public void updateProductById(UpdateProductRequestDto productRequestDto, Long productId) {

        Product product = findProductById(productId);

        product.setTitle(productRequestDto.getTitle());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());

        Category category = categoryService.findCategoryById(productRequestDto.getCategoryId());
        product.setCategory(category);

        product.setQty(productRequestDto.getQty());
        product.setImageUrl(productRequestDto.getImageUrl());

        productRepository.save(product);

    }

    @Override
    @Transactional
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
            Category category = categoryService.findCategoryById(productRequestDto.getCategoryId());
            product.setCategory(category);
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
    @Transactional
    public void deleteProductById(Long productId) {

        Product product = findProductById(productId);

        productRepository.delete(product);
    }

    @Override
    @Transactional
    public void softDeleteProductById(Long productId) {

        Product product = findProductById(productId);
        product.setDeleted(true);
        productRepository.save(product);
    }


}
