package com.zoomania.zoomania.model.dto;

import com.zoomania.zoomania.model.enums.CategoryEnum;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class CreateOrUpdateOfferDTO {
    @NotEmpty
    @Size(min = 3, max = 20)
    private String title;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotNull
    private CategoryEnum category;
    @NotEmpty
    @URL
    private String imageUrl;
    @NotEmpty
    @Size(min = 3, max = 150)
    private String description;

    public String getImageUrl() {
        return imageUrl;
    }

    public CreateOrUpdateOfferDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CreateOrUpdateOfferDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CreateOrUpdateOfferDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public CreateOrUpdateOfferDTO setCategory(CategoryEnum category) {
        this.category = category;
        return this;
    }



    public String getDescription() {
        return description;
    }

    public CreateOrUpdateOfferDTO setDescription(String description) {
        this.description = description;
        return this;
    }
}
