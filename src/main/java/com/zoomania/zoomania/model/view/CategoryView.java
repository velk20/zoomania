package com.zoomania.zoomania.model.view;

public class CategoryView {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public CategoryView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryView setName(String name) {
        this.name = name;
        return this;
    }
}
