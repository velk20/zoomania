package com.zoomania.zoomania.service;

import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.enums.CategoryEnum;
import com.zoomania.zoomania.model.view.CategoryView;
import com.zoomania.zoomania.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public List<CategoryView> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c->mapper.map(c, CategoryView.class))
                .collect(Collectors.toList());
    }

    public Optional<CategoryEntity> findByName(CategoryEnum categoryEnum) {
        return this.categoryRepository.findByName(categoryEnum);
    }

    public CategoryRepository getRepository() {
        return this.categoryRepository;
    }
}
