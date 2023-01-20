package com.zoomania.zoomania.model.dto.offer;

import com.zoomania.zoomania.model.enums.CategoryEnum;
import com.zoomania.zoomania.util.validation.ValidImageFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class UpdateOfferDTO {
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
    private String imageUrl;
    @ValidImageFormat
    private MultipartFile image;
    @NotEmpty(message = "Description can not be empty.")
    @Size(min = 3, max = 150)
    private String description;


    public String getBreed() {
        return breed;
    }

    public UpdateOfferDTO setBreed(String breed) {
        this.breed = breed;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public UpdateOfferDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public MultipartFile getImage() {
        return image;
    }

    public UpdateOfferDTO setImage(MultipartFile image) {
        this.image = image;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public UpdateOfferDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public UpdateOfferDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public UpdateOfferDTO setCategory(CategoryEnum category) {
        this.category = category;
        return this;
    }



    public String getDescription() {
        return description;
    }

    public UpdateOfferDTO setDescription(String description) {
        this.description = description;
        return this;
    }
}
