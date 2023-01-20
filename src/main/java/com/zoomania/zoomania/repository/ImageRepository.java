package com.zoomania.zoomania.repository;

import com.zoomania.zoomania.model.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    Optional<ImageEntity> findByPublicId(String publicId);

    Optional<ImageEntity> findByImageUrl(String imageUrl);
}
