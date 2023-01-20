package com.zoomania.zoomania.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "offer_images")
public class ImageEntity extends BaseEntity {
    @Column(nullable = false,unique = true)
    private String publicId;
    @Column(nullable = false, unique = true)
    private String imageUrl;
    @ManyToOne
    private OfferEntity offer;

    public OfferEntity getOffer() {
        return offer;
    }

    public ImageEntity setOffer(OfferEntity offer) {
        this.offer = offer;
        return this;
    }

    public ImageEntity() {
    }

    public ImageEntity(Long id, String publicId, String imageUrl) {
        super(id);
        this.publicId = publicId;
        this.imageUrl = imageUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public ImageEntity setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ImageEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
