package com.zoomania.zoomania.repository;

import com.zoomania.zoomania.model.entity.OfferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends
        JpaRepository<OfferEntity,Long>,
        JpaSpecificationExecutor<OfferEntity> {
    List<OfferEntity> findByOrderByCreatedOnDesc();
    Page<OfferEntity> findAllBySellerUsername(String username, Pageable pageable);
}
