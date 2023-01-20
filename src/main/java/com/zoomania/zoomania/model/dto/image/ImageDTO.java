package com.zoomania.zoomania.model.dto.image;

public class ImageDTO {
    private String publicId;
    private String imageUrl;

    public String getPublicId() {
        return publicId;
    }

    public ImageDTO setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ImageDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
