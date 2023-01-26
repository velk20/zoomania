package com.zoomania.zoomania.model.dto.offer;

import com.zoomania.zoomania.model.enums.CategoryEnum;

import javax.validation.constraints.Positive;

public class SearchOfferDTO {
    private String name;
    @Positive
    private Integer minPrice;
    @Positive
    private Integer maxPrice;
    private CategoryEnum category;

    public CategoryEnum getCategory() {
        return category;
    }

    public SearchOfferDTO setCategory(CategoryEnum category) {
        this.category = category;
        return this;
    }

    public String getName() {
        return name;
    }

    public SearchOfferDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public SearchOfferDTO setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
        return this;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public SearchOfferDTO setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }

    public boolean isEmpty() {
        return (name == null || name.isEmpty()) &&
                minPrice == null &&
                maxPrice == null;
    }

    @Override
    public String toString() {
        return "SearchOfferDTO{" +
                "name='" + name + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                '}';
    }


}