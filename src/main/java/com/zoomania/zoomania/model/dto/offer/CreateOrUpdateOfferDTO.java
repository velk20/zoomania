package com.zoomania.zoomania.model.dto.offer;

import com.zoomania.zoomania.model.enums.CategoryEnum;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class CreateOrUpdateOfferDTO {
    @NotEmpty(message = "Title can not be empty.")
    @Size(min = 3, max = 50)
    private String title;
    @NotEmpty(message = "Breed can not be empty.")
    @Size(min = 3, max = 50)
    private String breed;
    @NotNull(message = "Price can not be empty.")
    @DecimalMin(value = "0")
    private BigDecimal price;
    @NotNull(message = "Category can not be empty.")
    private CategoryEnum category;
    @NotEmpty(message = "Image Url can not be empty.")
    @URL
    private String imageUrl;
    @NotEmpty(message = "Description can not be empty.")
    @Size(min = 3, max = 150)
    private String description;


    public String getBreed() {
        return breed;
    }

    public CreateOrUpdateOfferDTO setBreed(String breed) {
        this.breed = breed;
        return this;
    }

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
