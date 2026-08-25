package com.ecommerceproject.productservice.specifications;

import com.ecommerceproject.productservice.models.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecifications {

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (productRoot,
                criteriaQuery,
                criteriaBuilder)
                    -> criteriaBuilder.equal(productRoot.get("category").get("id"), categoryId);
    }

    public static Specification<Product> isNotDeleted() {
        return (productRoot,
                criteriaQuery,
                criteriaBuilder)
                    -> criteriaBuilder.equal(productRoot.get("isDeleted"), false);
    }

    public static Specification<Product> containsTitle(String search) {
        return (productRoot,
                query,
                criteriaBuiler)
                -> criteriaBuiler.like(criteriaBuiler.lower(productRoot.get("title")),
                "%" + search.toLowerCase() + "%" );
    }

    public static Specification<Product> priceGreaterThanOrEqualTo(BigDecimal minPrice) {
        return (productRoot,
                query,
                criteriaBuilder)
                -> criteriaBuilder.greaterThanOrEqualTo(productRoot.get("price"), minPrice);
    }

    public static Specification<Product> priceLesserThanOrEqualTo(BigDecimal maxPrice) {
        return (productRoot,
                query,
                criteriaBuilder)
                -> criteriaBuilder.lessThanOrEqualTo(productRoot.get("price"), maxPrice);
    }
}
