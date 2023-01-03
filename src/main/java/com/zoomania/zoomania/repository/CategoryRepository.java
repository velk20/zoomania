package com.zoomania.zoomania.repository;

import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(CategoryEnum categoryEnum);
}
