package com.zoomania.zoomania.util;

import com.zoomania.zoomania.model.entity.*;
import com.zoomania.zoomania.model.enums.CategoryEnum;
import com.zoomania.zoomania.model.enums.UserRoleEnum;
import com.zoomania.zoomania.repository.*;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestDataUtils {
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ImageRepository imageRepository;

    public TestDataUtils(CategoryRepository categoryRepository, CommentRepository commentRepository, OfferRepository offerRepository, UserRepository userRepository, UserRoleRepository userRoleRepository, ImageRepository imageRepository) {
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.imageRepository = imageRepository;
    }

    private void  initUserRoles() {
        if (userRoleRepository.count() == 0) {
            UserRoleEntity adminRole = new UserRoleEntity().setUserRoleEnum(UserRoleEnum.ADMIN);
            UserRoleEntity userRole = new UserRoleEntity().setUserRoleEnum(UserRoleEnum.USER);

            userRoleRepository.save(adminRole);
            userRoleRepository.save(userRole);
        }
    }

    public List<CategoryEntity> initCategory() {
        if (categoryRepository.count() == 0) {
            Arrays.stream(CategoryEnum.values())
                    .forEach(
                            c -> {
                                CategoryEntity categoryEntity = new CategoryEntity().setName(c);
                                categoryRepository.save(categoryEntity);
                            }
                    );

        }
        return categoryRepository.findAll();
    }


    public UserEntity createTestAdmin(String username) {
        initUserRoles();

        UserEntity userEntity = new UserEntity()
                .setActive(true)
                .setUserRoles(userRoleRepository.findAll())
                .setAge(32)
                .setEmail("admin@admin.com")
                .setPassword("feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c")
                .setFirstName("Admin")
                .setLastName("Adminov")
                .setPhone("0888888")
                .setUsername(username);

        return userRepository.save(userEntity);
    }

    public UserEntity createTestUser(String username) {
        initUserRoles();

        UserEntity userEntity = new UserEntity()
                .setActive(true)
                .setUserRoles(userRoleRepository.findAll()
                        .stream()
                        .filter(r->r.getUserRoleEnum() != UserRoleEnum.ADMIN)
                        .collect(Collectors.toList()))
                .setAge(32)
                .setEmail("user@user.com")
                .setPassword("feacb029fda31fcea2038d31933e65929fa6a2f635dd0563947147c9245504a4db4400ff6f59ea8c")
                .setFirstName("User")
                .setLastName("Userov")
                .setPhone("088888869")
                .setUsername(username);

        return userRepository.save(userEntity);
    }

    @Transactional
    public OfferEntity createTestOffer(UserEntity seller,CategoryEntity category) {
        OfferEntity offerEntity = new OfferEntity()
                .setCategory(category)
                .setCreatedOn(LocalDateTime.now())
                .setBreed("Husky")
                .setPrice(BigDecimal.valueOf(213.45))
                .setTitle("Dog like hulk")
                .setDescription("Cutstom desc")
                .setSeller(this.userRepository.findByUsername(seller.getUsername()).get());

        OfferEntity saved = offerRepository.save(offerEntity);
        ImageEntity testImage = this.createTestImage(saved);
        saved.addImage(testImage);

       return this.offerRepository.save(saved);
    }

    public ImageEntity createTestImage(OfferEntity offer) {
        return this.imageRepository.save(new ImageEntity()
                .setImageUrl("https://image.com/image.png"+offer.getId())
                .setOffer(offer)
                .setPublicId("publicId"+offer.getId()));
    }

    public CommentEntity createTestComment(UserEntity author, OfferEntity offer) {
        CommentEntity commentEntity = new CommentEntity()
                .setCreated(LocalDateTime.now())
                .setAuthor(author)
                .setOffer(offer)
                .setText("Custom comment!");

        return commentRepository.save(commentEntity);
    }

    public void cleanUpDatabase() {
        commentRepository.deleteAll();
        offerRepository.deleteAll();
        userRepository.deleteAll();
        userRoleRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
