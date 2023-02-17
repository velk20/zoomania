package com.zoomania.zoomania.service;

import com.sun.security.auth.UserPrincipal;
import com.zoomania.zoomania.model.dto.offer.CreateOfferDTO;
import com.zoomania.zoomania.model.dto.user.ChangeUserPasswordDTO;
import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.enums.CategoryEnum;
import com.zoomania.zoomania.model.user.ZooManiaUserDetails;
import com.zoomania.zoomania.model.view.OfferDetailsView;
import com.zoomania.zoomania.model.view.UserDetailsView;
import com.zoomania.zoomania.repository.OfferRepository;
import com.zoomania.zoomania.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)

public class OfferServiceTest {
    @Mock
    private OfferRepository offerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ImageService imageService;
    @Mock
    private ModelMapper mapper;
    @Mock
    private CloudinaryService cloudinaryService;

    private static final String TEST_USER_USERNAME = "admin";
    private OfferService offerService;
    private UserEntity userEntity;
    private Principal principal = new UserPrincipal("admin");
    private MultipartFile multipartFile = new MockMultipartFile("file", "file", "image/jpg", "file".getBytes());
    private MultipartFile[] files = new MultipartFile[]{multipartFile};

    private CreateOfferDTO createOfferDTO = new CreateOfferDTO(
            "Offer new",
            "My breed",
            BigDecimal.TEN,
            CategoryEnum.Dogs,
            files,
            "Description"
    );

    private UserDetails userDetails = new ZooManiaUserDetails(
            1L,
            "feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c",
            TEST_USER_USERNAME,
            "Admin",
            "Adminov",
            true,
            Collections.emptyList()
    );

    private CategoryEntity category = new CategoryEntity(
            1L,
            CategoryEnum.Dogs
    );

    private OfferEntity offerEntity = new OfferEntity(
            1L,
            "Offer new",
            "Description",
            BigDecimal.TEN,
            "Husky",
            LocalDateTime.now(),
            true,
            category,
            userEntity,
            Collections.emptyList()
    );

    private ChangeUserPasswordDTO changeUserPasswordDTO =
            new ChangeUserPasswordDTO()
                    .setConfirmPassword("admin2")
                    .setNewPassword("admin2")
                    .setOldPassword("admin");
    private UserDetailsView userDetailsView = new UserDetailsView()
            .setEmail("dsa@gmail.com")
            .setUsername(TEST_USER_USERNAME);


    @BeforeEach
    void setUp() {
        offerService = new OfferService(offerRepository, userRepository, categoryService, imageService, mapper, cloudinaryService);
        userEntity = new UserEntity()
                .setUsername(TEST_USER_USERNAME)
                .setEmail("dsa@gmail.com")
                .setPassword("strongPassword")
                .setPhone("6565656");
    }

//    @Test
//    void getOfferByIdTest() {
//        when(offerRepository.findById(offerEntity.getId()))
//                .thenReturn(Optional.ofNullable(offerEntity));
//
//        OfferEntity offer = offerRepository.findById(offerEntity.getId()).get();
//        offer.setSeller(userEntity);
//
//        when(offer.getSeller().getId())
//                .thenReturn(userEntity.getId());
//
//        when(userRepository.findById(offer.getSeller().getId()))
//                .thenReturn(Optional.ofNullable(userEntity));
//
//        UserEntity user = userRepository.findById(offer.getSeller().getId()).get();
//
//        OfferDetailsView offerById = offerService.getOfferById(offerEntity.getId());
//        assertEquals(offerById.getTitle(), offer.getTitle());
//    }
}
