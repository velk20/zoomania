package com.zoomania.zoomania.model.dto;

import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.enums.CategoryEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OfferDetailsDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private LocalDate createdOn;
    private CategoryEnum category;
    private String sellerFirstName;
    private String sellerLastName;

    public String getDescription() {
        return description;
    }

    public OfferDetailsDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public OfferDetailsDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public OfferDetailsDTO setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public OfferDetailsDTO setCategory(CategoryEnum category) {
        this.category = category;
        return this;
    }

    public String getSellerFirstName() {
        return sellerFirstName;
    }

    public OfferDetailsDTO setSellerFirstName(String sellerFirstName) {
        this.sellerFirstName = sellerFirstName;
        return this;
    }

    public String getSellerLastName() {
        return sellerLastName;
    }

    public OfferDetailsDTO setSellerLastName(String sellerLastName) {
        this.sellerLastName = sellerLastName;
        return this;
    }

    public Long getId() {
        return id;
    }

    public OfferDetailsDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public OfferDetailsDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OfferDetailsDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
