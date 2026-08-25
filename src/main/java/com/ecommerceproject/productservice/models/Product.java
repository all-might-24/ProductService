package com.ecommerceproject.productservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity(name = "products")
public class Product extends BaseEntity{

    private String title;
    private String description;
    private Integer qty;
    private BigDecimal price;
    private String imageUrl;
    private boolean isDeleted = false;
    @ManyToOne
    private Category category;
}
