package com.zoomania.zoomania.repository;

import com.zoomania.zoomania.model.dto.offer.SearchOfferDTO;
import com.zoomania.zoomania.model.entity.OfferEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;

public class OfferSpecification implements Specification<OfferEntity> {
    private final SearchOfferDTO searchOfferDTO;

    public OfferSpecification(SearchOfferDTO searchOfferDTO) {
        this.searchOfferDTO = searchOfferDTO;
    }

    @Override
    public Predicate toPredicate(Root<OfferEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (searchOfferDTO.getName() != null && !searchOfferDTO.getName().isEmpty()) {
            predicate.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.like(root.get("title"),"%"+searchOfferDTO.getName()+"%"))
            );
        }

        if (searchOfferDTO.getMinPrice() != null) {
            predicate.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), searchOfferDTO.getMinPrice()))
            );
        }

        if (searchOfferDTO.getMaxPrice() != null) {
            predicate.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("price"), searchOfferDTO.getMaxPrice()))
            );
        }

        return predicate;
    }
}
