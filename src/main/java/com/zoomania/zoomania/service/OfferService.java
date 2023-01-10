package com.zoomania.zoomania.service;

import com.zoomania.zoomania.model.dto.CreateOrUpdateOfferDTO;
import com.zoomania.zoomania.model.dto.OfferDetailsDTO;
import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.repository.CategoryRepository;
import com.zoomania.zoomania.repository.OfferRepository;
import com.zoomania.zoomania.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final CategoryRepository categoryRepository;
    private final UserDetailsService userDetailsService;

    public OfferService(OfferRepository offerRepository,
                        UserRepository userRepository,
                        ModelMapper mapper,
                        CategoryRepository categoryRepository,
                        UserDetailsService userDetailsService) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
        this.userDetailsService = userDetailsService;
    }


    public void addOffer(CreateOrUpdateOfferDTO addOfferDTO, UserDetails userDetails) {
        OfferEntity newOffer = mapper.map(addOfferDTO, OfferEntity.class);

        UserEntity seller = userRepository.findByUsername(userDetails.getUsername()).
                orElseThrow();

        CategoryEntity categoryEntity = categoryRepository.findByName(addOfferDTO.getCategory()).orElseThrow();

        newOffer
                .setCategory(categoryEntity)
                .setSeller(seller)
                .setCreatedOn(LocalDateTime.now());

        offerRepository.save(newOffer);
    }

    public List<OfferDetailsDTO> getRecentOffers(int limit) {
        List<OfferEntity> byOrderByCreatedOnDesc =
                this.offerRepository.findByOrderByCreatedOnDesc();
        if (byOrderByCreatedOnDesc == null || byOrderByCreatedOnDesc.size() == 0) {
            return null;
        }
        if (byOrderByCreatedOnDesc.size() < limit) {
            return byOrderByCreatedOnDesc
                    .stream()
                    .map(this::map)
                    .collect(Collectors.toList());
        }

        return byOrderByCreatedOnDesc.subList(0, limit)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public Page<OfferDetailsDTO> getAllOffers(Pageable pageable) {
        return this.offerRepository
                .findAll(pageable)
                .map(this::map);
    }

    public Page<OfferDetailsDTO> getAllUserOffers(String username, Pageable pageable) {

        return this.offerRepository.findAllBySellerUsername(username, pageable)
                .map(this::map);
    }

    public CreateOrUpdateOfferDTO getEditOfferById(Long id) {
        OfferEntity offerEntity = this.offerRepository
                .findById(id)
                .orElseThrow(RuntimeException::new);

        return mapper.map(offerEntity, CreateOrUpdateOfferDTO.class);
    }

    public OfferDetailsDTO getOfferById(Long id) {
        OfferEntity offerEntity = this.offerRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        UserEntity seller = this.userRepository.findById(offerEntity.getSeller().getId()).orElseThrow(RuntimeException::new);

        OfferDetailsDTO offerDetailsDTO = map(offerEntity);

        return offerDetailsDTO
                .setSellerFirstName(seller.getFirstName())
                .setSellerLastName(seller.getLastName())
                .setSellerUsername(seller.getUsername());
    }

    private OfferDetailsDTO map(OfferEntity offerEntity) {
        return mapper.map(offerEntity, OfferDetailsDTO.class);
    }

    public boolean deleteOfferById(Long id) {
        Optional<OfferEntity> byId = this.offerRepository.findById(id);
        if (byId.isPresent()) {
            this.offerRepository.delete(byId.get());
            return true;
        }
        return false;
    }

    public void editOffer(Long id,CreateOrUpdateOfferDTO editOffer) {
        Optional<OfferEntity> offerById = this.offerRepository.findById(id);
        if (offerById.isEmpty()) {
            throw new RuntimeException("No offer found!");
        }

        CategoryEntity categoryEntity = categoryRepository.findByName(editOffer.getCategory()).orElseThrow();

        OfferEntity offerEntity = offerById.get();
        offerEntity
                .setTitle(editOffer.getTitle())
                .setBreed(editOffer.getBreed())
                .setPrice(editOffer.getPrice())
                .setDescription(editOffer.getDescription())
                .setImageUrl(editOffer.getImageUrl())
                .setCategory(categoryEntity);

        this.offerRepository.save(offerEntity);
    }
}
