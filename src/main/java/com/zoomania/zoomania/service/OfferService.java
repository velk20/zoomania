package com.zoomania.zoomania.service;

import com.zoomania.zoomania.exceptions.*;
import com.zoomania.zoomania.model.dto.offer.CreateOfferDTO;
import com.zoomania.zoomania.model.dto.offer.SearchOfferDTO;
import com.zoomania.zoomania.model.dto.offer.UpdateOfferDTO;
import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.entity.ImageEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.enums.UserRoleEnum;
import com.zoomania.zoomania.model.view.OfferDetailsView;
import com.zoomania.zoomania.model.view.OfferResponse;
import com.zoomania.zoomania.repository.OfferRepository;
import com.zoomania.zoomania.repository.OfferSpecification;
import com.zoomania.zoomania.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final ModelMapper mapper;
    private final CloudinaryService cloudinaryService;

    public OfferService(OfferRepository offerRepository, UserRepository userRepository, CategoryService categoryService, ImageService imageService, ModelMapper mapper, CloudinaryService cloudinaryService) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.mapper = mapper;
        this.cloudinaryService = cloudinaryService;
    }
    @Transactional
    public void addOffer(CreateOfferDTO addOfferDTO, UserDetails userDetails) {
        OfferEntity newOffer = mapper.map(addOfferDTO, OfferEntity.class);

        UserEntity seller = userRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        CategoryEntity categoryEntity = categoryService
                .findByName(addOfferDTO.getCategory())
                .orElseThrow(CategoryNotFoundException::new);

        List<ImageEntity> imageEntityList =
                Arrays.stream(addOfferDTO.getImageUrl())
                .map(this::uploadImageEntity)
                .toList();

        imageEntityList.forEach(e->e.setOffer(newOffer));

        this.imageService.saveAll(imageEntityList);

        newOffer
                .setImagesEntities(imageEntityList)
                .setCategory(categoryEntity)
                .setSeller(seller)
                .setActive(false)
                .setCreatedOn(LocalDateTime.now());

        if (userDetails.getAuthorities()
                .stream()
                .anyMatch(o->o.getAuthority().equals("ROLE_ADMIN"))) {
            newOffer.setActive(true);
        }
        offerRepository.save(newOffer);
    }

    public List<OfferDetailsView> getRecentOffers(int limit) {
        List<OfferEntity> byOrderByCreatedOnDesc =
                this.offerRepository
                        .findByOrderByCreatedOnDesc()
                        .stream()
                        .filter(OfferEntity::isActive)
                        .toList();

        if (byOrderByCreatedOnDesc.size() == 0) {
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

    public Page<OfferDetailsView> getAllOffers(Pageable pageable) {
        return  this.offerRepository
                .findAllByIsActive(true,pageable)
                .map(this::map);
    }

    public Page<OfferDetailsView> getAllOffers(Pageable pageable,boolean isActive) {
        return  this.offerRepository
                .findAllByIsActive(isActive,pageable)
                .map(this::map);
    }

    public Page<OfferDetailsView> getAllUserOffers(String username, Pageable pageable) {
        this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, username)));
        return this.offerRepository
                .findAllBySellerUsername(username, pageable)
                .map(this::map);
    }

    public List<OfferEntity> getAllOffersBySeller(UserEntity seller) {
        return this.offerRepository.findAllBySeller(seller);
    }

    public UpdateOfferDTO getEditOfferById(Long id) {
        OfferEntity offerEntity =
                this.offerRepository
                .findById(id)
                .orElseThrow(() -> new OfferNotFoundException(id));

        OfferDetailsView map = mapper.map(offerEntity, OfferDetailsView.class);

        UpdateOfferDTO updateOfferDTO = mapper.map(map, UpdateOfferDTO.class);
        updateOfferDTO.setImageUrl(offerEntity.getImagesEntities().get(0).getImageUrl());
        return updateOfferDTO;
    }

    public OfferDetailsView getOfferById(Long id) {
        OfferEntity offerEntity = this.offerRepository
                .findById(id)
                .orElseThrow(() -> new OfferNotFoundException(id));

        UserEntity seller = this.userRepository
                .findById(offerEntity.getSeller().getId())
                .orElseThrow(UserNotFoundException::new);

        OfferDetailsView offerDetailsView = map(offerEntity);

        return offerDetailsView
                .setSellerFirstName(seller.getFirstName())
                .setSellerLastName(seller.getLastName())
                .setSellerUsername(seller.getUsername())
                .setSellerEmail(seller.getEmail())
                .setSellerPhone(seller.getPhone());
    }

    public boolean isOwner(String username, Long offerId) {
        boolean isOwner = offerRepository.
                findById(offerId).
                filter(o -> o.getSeller().getUsername().equals(username)).
                isPresent();

        if (isOwner) {
            return true;
        }

        return userRepository.
                findByUsername(username).
                filter(this::isAdmin).
                isPresent();
    }
    private boolean isAdmin(UserEntity user) {
        return user.getUserRoles().
                stream().
                anyMatch(r -> r.getUserRoleEnum() == UserRoleEnum.ADMIN);
    }
    private OfferDetailsView map(OfferEntity offerEntity) {
        return mapper.map(offerEntity, OfferDetailsView.class)
                .setImagesUrls(
                        offerEntity.getImagesEntities()
                        .stream()
                        .map(ImageEntity::getImageUrl)
                                .collect(Collectors.toList()));
    }

    @Transactional
    public void deleteOfferById(Long id) {
        OfferEntity offer = this.offerRepository
                .findById(id)
                .orElseThrow(() -> new OfferNotFoundException(id));

        deleteOfferImageCloudinary(offer);
        offer.setSeller(null);
        this.offerRepository.save(offer);
        this.offerRepository.delete(offer);
    }

    @Transactional
    public void editOffer(Long id, UpdateOfferDTO editOffer) {
        OfferEntity offerById = this.offerRepository
                .findById(id)
                .orElseThrow(() -> new OfferNotFoundException(id));

        CategoryEntity categoryEntity = categoryService
                .findByName(editOffer.getCategory())
                .orElseThrow(CategoryNotFoundException::new);

        offerById
                .setTitle(editOffer.getTitle())
                .setBreed(editOffer.getBreed())
                .setPrice(editOffer.getPrice())
                .setDescription(editOffer.getDescription())
                .setCategory(categoryEntity);

        MultipartFile[] newImages = editOffer.getImages();
        if (newImages.length > 0 && !newImages[0].getOriginalFilename().isEmpty()) {
            deleteOldImagesAndUploadNewOnes(offerById, newImages);
        }

        this.offerRepository.save(offerById);
    }

    private void deleteOldImagesAndUploadNewOnes(OfferEntity offerById, MultipartFile[] newImages) {
        deleteOfferImageCloudinary(offerById);

        this.imageService.deleteAll(offerById.getImagesEntities());
        offerById.removeAllImages();

        List<ImageEntity> newImagesEntities =
                Arrays.stream(newImages)
                        .map(this::uploadImageEntity)
                        .collect(Collectors.toList());
        newImagesEntities.forEach(i -> i.setOffer(offerById));

        offerById.setImagesEntities(newImagesEntities);
        this.imageService.saveAll(newImagesEntities)
                 .forEach(offerById::addImage);
    }

    private void deleteOfferImageCloudinary(OfferEntity offerById) {
        for (ImageEntity imageEntity : offerById.getImagesEntities()) {
            try {
                this.cloudinaryService.deletePhoto(imageEntity);
            } catch (IOException e) {
                throw new ImageNotFoundException();
            }
        }
    }

    private ImageEntity uploadImageEntity(MultipartFile image) {
        ImageEntity imageEntity = null;
        try {
             imageEntity = this.cloudinaryService.uploadPhoto(image);
        } catch (IOException e) {
            throw new ImageNotFoundException(e.getMessage());
        }
        return imageEntity;
    }

    public Page<OfferDetailsView> searchOffer(SearchOfferDTO searchOfferDTO,Pageable pageable) {
        return this.offerRepository
                .findAll(new OfferSpecification(searchOfferDTO, categoryService.getRepository()), pageable)
                .map(this::map);
    }

    public OfferResponse getAllOffersAdminRest(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);

        Page<OfferDetailsView> offers = this.getAllOffers(pageable,true);

        // get content for page object
        List<OfferDetailsView> listOfOffers = offers.getContent();

        return new OfferResponse()
                .setContent(listOfOffers)
                .setPageNo(offers.getNumber())
                .setPageSize(offers.getSize())
                .setTotalElements(offers.getTotalElements())
                .setTotalPages(offers.getTotalPages())
                .setLast(offers.isLast());
    }

    private static Pageable getPageable(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        return PageRequest.of(pageNo, pageSize, sort);
    }

    public long getAllOffersCount() {
        return this.offerRepository.count();
    }

    public OfferResponse getAllNotApproveOffers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);

        Page<OfferDetailsView> offers = this.getAllOffers(pageable,false);

        // get content for page object
        List<OfferDetailsView> listOfOffers = offers.getContent();

        return new OfferResponse()
                .setContent(listOfOffers)
                .setPageNo(offers.getNumber())
                .setPageSize(offers.getSize())
                .setTotalElements(offers.getTotalElements())
                .setTotalPages(offers.getTotalPages())
                .setLast(offers.isLast());

    }

    public void approveOfferById(Long id) {
        Optional<OfferEntity> offerEntity = this.offerRepository.findById(id);
        offerEntity.ifPresent(offer -> this.offerRepository.save(offer.setActive(true).setCreatedOn(LocalDateTime.now())));
    }

    public Optional<OfferEntity> findById(Long offerId) {
        return this.offerRepository.findById(offerId);
    }
}
