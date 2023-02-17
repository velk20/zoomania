package com.zoomania.zoomania.service;

import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.enums.CategoryEnum;
import com.zoomania.zoomania.model.view.CategoryView;
import com.zoomania.zoomania.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ModelMapper mapper;

    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        this.categoryService = new CategoryService(categoryRepository, mapper);
    }

    @Test
    public void getAllCategoriesTest() {
        CategoryEntity categoryEntityDog = new CategoryEntity(1L, CategoryEnum.Dogs);
        CategoryEntity categoryEntityCat = new CategoryEntity(2L, CategoryEnum.Cats);

        List<CategoryEntity> entities = List.of(categoryEntityDog, categoryEntityCat);

        when(categoryRepository.findAll())
                .thenReturn(entities);

        when(mapper.map(categoryEntityDog, CategoryView.class))
                .thenReturn(new CategoryView()
                        .setId(categoryEntityDog.getId())
                        .setName(categoryEntityDog.getName().name()));

        when(mapper.map(categoryEntityCat, CategoryView.class))
                .thenReturn(new CategoryView()
                        .setId(categoryEntityCat.getId())
                        .setName(categoryEntityCat.getName().name()));

        List<CategoryView> allCategories = this.categoryService.getAllCategories();
        assertEquals(entities.size(), allCategories.size());
        assertTrue(allCategories.stream().anyMatch(c-> c.getId().equals(categoryEntityDog.getId())));
        assertTrue(allCategories.stream().anyMatch(c-> c.getId().equals(categoryEntityCat.getId())));
    }
}
