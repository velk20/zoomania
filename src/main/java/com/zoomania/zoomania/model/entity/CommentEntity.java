package com.zoomania.zoomania.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity{
    private LocalDateTime created;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    @ManyToOne
    private UserEntity author;
    @ManyToOne
    private OfferEntity offer;

    public LocalDateTime getCreated() {
        return created;
    }

    public CommentEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public String getText() {
        return text;
    }

    public CommentEntity setText(String text) {
        this.text = text;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public CommentEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public OfferEntity getOffer() {
        return offer;
    }

    public CommentEntity setOffer(OfferEntity offer) {
        this.offer = offer;
        return this;
    }
}

