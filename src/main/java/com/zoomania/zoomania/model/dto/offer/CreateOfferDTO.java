package com.zoomania.zoomania.model.dto.offer;

import com.zoomania.zoomania.model.enums.CategoryEnum;
import com.zoomania.zoomania.util.validation.NotEmptyImage;
import com.zoomania.zoomania.util.validation.ValidImageFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class CreateOfferDTO {
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
    @NotEmptyImage(message = "Image is required.")
    @ValidImageFormat(message = "Invalid image format.")
    private MultipartFile[] imageUrl;
    @NotEmpty(message = "Description can not be empty.")
    @Size(min = 3, max = 150)
    private String description;


    public String getBreed() {
        return breed;
    }

    public CreateOfferDTO setBreed(String breed) {
        this.breed = breed;
        return this;
    }

    public MultipartFile[] getImageUrl() {
        return imageUrl;
    }

    public CreateOfferDTO setImageUrl(MultipartFile[] imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CreateOfferDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CreateOfferDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public CreateOfferDTO setCategory(CategoryEnum category) {
        this.category = category;
        return this;
    }



    public String getDescription() {
        return description;
    }

    public CreateOfferDTO setDescription(String description) {
        this.description = description;
        return this;
    }
}
