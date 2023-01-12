package com.zoomania.zoomania.repository;

import com.zoomania.zoomania.model.entity.CommentEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    Optional<List<CommentEntity>> findAllByOffer(OfferEntity offer);
}
