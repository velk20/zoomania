package com.zoomania.zoomania.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "offers")
public class OfferEntity extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    @Basic
    private String imageUrl;
    @Column(nullable = false)
    private LocalDate createdOn;
    @ManyToOne(optional = false)
    private CategoryEntity category;
    @ManyToOne(optional = false)
    private UserEntity seller;

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public OfferEntity setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public OfferEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public OfferEntity setCategory(CategoryEntity category) {
        this.category = category;
        return this;
    }

    public UserEntity getSeller() {
        return seller;
    }

    public OfferEntity setSeller(UserEntity seller) {
        this.seller = seller;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public OfferEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public OfferEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OfferEntity setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
