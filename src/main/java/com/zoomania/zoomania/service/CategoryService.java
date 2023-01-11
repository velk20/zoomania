package com.zoomania.zoomania.service;

import com.zoomania.zoomania.model.view.CategoryView;
import com.zoomania.zoomania.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
