package com.zoomania.zoomania.service;

import com.zoomania.zoomania.model.entity.ImageEntity;
import com.zoomania.zoomania.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    private final Long INITIAL_LAST_IMAGE_ENTITY_ID = 29L;
    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;

    public ImageService(ImageRepository imageRepository, CloudinaryService cloudinaryService) {
        this.imageRepository = imageRepository;
        this.cloudinaryService = cloudinaryService;
    }
    public List<ImageEntity> saveAll(List<ImageEntity> imageEntities) {
        return this.imageRepository.saveAll(imageEntities);
    }

    public void deleteAll(List<ImageEntity> imageEntities) {
         this.imageRepository.deleteAll(imageEntities);
    }

    public void deleteAllNotInitialPhotos() {
        List<ImageEntity> allByIdGreater =
                this.imageRepository
                .findAllByIdGreaterThan(INITIAL_LAST_IMAGE_ENTITY_ID);
        for (ImageEntity imageEntity : allByIdGreater) {
            try {
                cloudinaryService.deletePhoto(imageEntity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
