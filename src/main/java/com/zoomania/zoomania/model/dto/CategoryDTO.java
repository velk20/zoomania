package com.zoomania.zoomania.model.dto;

public class CategoryDTO {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public CategoryDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryDTO setName(String name) {
        this.name = name;
        return this;
    }
}
