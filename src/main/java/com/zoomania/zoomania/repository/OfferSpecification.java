package com.zoomania.zoomania.repository;

import com.zoomania.zoomania.model.dto.offer.SearchOfferDTO;
import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class OfferSpecification implements Specification<OfferEntity> {
    private final SearchOfferDTO searchOfferDTO;
    private final CategoryRepository categoryRepository;

    public OfferSpecification(SearchOfferDTO searchOfferDTO,  CategoryRepository categoryRepository) {
        this.searchOfferDTO = searchOfferDTO;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Predicate toPredicate(Root<OfferEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();
        predicate.getExpressions().add(
                criteriaBuilder.and(criteriaBuilder.equal(root.get("isActive"),searchOfferDTO.isActive()))
        );

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

        if (searchOfferDTO.getCategory() != null) {
            Optional<CategoryEntity> categoryEntity = this.categoryRepository.findByName(searchOfferDTO.getCategory());
            predicate.getExpressions().add(
                    criteriaBuilder.and(criteriaBuilder.equal(root.get("category"),categoryEntity.get()))
            );
        }

        return predicate;
    }
}
