package com.zoomania.zoomania.service;

import com.zoomania.zoomania.model.entity.ImageEntity;
import com.zoomania.zoomania.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    // ! Make it work with multiple images
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public ImageEntity save(ImageEntity imageEntity) {
        return this.imageRepository.save(imageEntity);
    }

    public List<ImageEntity> saveAll(List<ImageEntity> imageEntities) {
        return this.imageRepository.saveAll(imageEntities);
    }

    public void deleteAll(List<ImageEntity> imageEntities) {
         this.imageRepository.deleteAll(imageEntities);
    }

}
