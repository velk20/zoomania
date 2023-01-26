package com.zoomania.zoomania.config;

import com.zoomania.zoomania.service.ImageService;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
public class ShutdownConfig {
    private final ImageService imageService;

    public ShutdownConfig(ImageService imageService) {
        this.imageService = imageService;
    }

    @PreDestroy
    public void onDestroy(){
        imageService.deleteAllNotInitialPhotos() ;
    }
}
