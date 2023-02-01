package com.zoomania.zoomania.model.entity;

import com.zoomania.zoomania.model.enums.CategoryEnum;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class  CategoryEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private CategoryEnum name;

    public CategoryEntity() {
    }

    public CategoryEntity(Long id, CategoryEnum name) {
        super(id);
        this.name = name;
    }

    public CategoryEnum getName() {
        return name;
    }

    public CategoryEntity setName(CategoryEnum categoryEnum) {
        this.name = categoryEnum;
        return this;
    }

    @Override
    public String toString() {
        return name.name();
    }
}
