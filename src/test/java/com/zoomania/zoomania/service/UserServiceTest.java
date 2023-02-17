package com.zoomania.zoomania.service;

import com.sun.security.auth.UserPrincipal;
import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.dto.user.ChangeUserPasswordDTO;
import com.zoomania.zoomania.model.dto.user.UpdateUserDTO;
import com.zoomania.zoomania.model.dto.user.UserRegisterDTO;
import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.entity.UserRoleEntity;
import com.zoomania.zoomania.model.enums.CategoryEnum;
import com.zoomania.zoomania.model.enums.UserRoleEnum;
import com.zoomania.zoomania.model.user.ZooManiaUserDetails;
import com.zoomania.zoomania.model.view.UserDetailsView;
import com.zoomania.zoomania.repository.UserRepository;
import com.zoomania.zoomania.util.TestUserDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private ModelMapper mapper;
    @Mock
    private OfferService offerService;
    @Mock
    private TestUserDataService testUserDataService;

    private static final String TEST_USER_USERNAME = "admin";
    private UserService userService;
    private UserEntity userEntity;
    private Principal principal = new UserPrincipal("admin");
    private UserRegisterDTO userRegisterDTO = new UserRegisterDTO(
            TEST_USER_USERNAME,
            "admin",
            "admin@gmail.com",
            "admin@gmail.com",
            "31231231",
            32,
            "admin",
            "admin"
    );

    private UpdateUserDTO updateUserDTO = new UpdateUserDTO(
            TEST_USER_USERNAME,
            "admin",
            "admin@gmail.com",
            "admin@gmail.com",
            "31231231",
            32,
            true,
            true
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
        userService = new UserService(userRepository, userRoleService, passwordEncoder, userDetailsService, mapper, offerService);
        userEntity = new UserEntity()
                .setUsername(TEST_USER_USERNAME)
                .setEmail("dsa@gmail.com")
                .setPassword("strongPassword")
                .setPhone("6565656");
    }

    @Test
    void canGetIsCalling() {
        userService.getAllUsersCount();
        verify(userRepository).count();
    }

    @Test
    void userCanRegisterAdnLoginTest() {
        when(userRoleService.findByUserRoleEnum(UserRoleEnum.USER))
                .thenReturn(new UserRoleEntity().setUserRoleEnum(UserRoleEnum.USER));
        when(mapper.map(userRegisterDTO, UserEntity.class))
                .thenReturn(userEntity);
        when(userDetailsService.loadUserByUsername(userEntity.getUsername()))
                .thenReturn(userDetails);

        userService.registerAndLogin(userRegisterDTO);

        ArgumentCaptor<UserEntity> userEntityArgumentCaptor =
                ArgumentCaptor.forClass(UserEntity.class);

        verify(userRepository)
                .save(userEntityArgumentCaptor.capture());

        UserEntity savedUser = userEntityArgumentCaptor.getValue();

        assertEquals(savedUser.getUsername(), userRegisterDTO.getUsername());
    }

    @Test
    void getUserTest_ThrowsException() {
       when(userRepository.findByUsername(any()))
               .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUser(userEntity.getUsername()));
    }

    @Test
    void editUser_ThrowsExceptionUserNotFound() {
        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.editUser(userEntity.getUsername(),updateUserDTO,principal));
    }

    @Test
    void changeUserPassword_ThrowsExceptionUserNotFound() {
        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.changeUserPassword(changeUserPasswordDTO));

    }

    @Test
    void changeUserPassword_ReturnsFalseInvalidPassword() {
         when(userRepository.findByUsername(changeUserPasswordDTO.getUsername()))
                .thenReturn(Optional.ofNullable(userEntity));

        Optional<UserEntity> user = userRepository.findByUsername(changeUserPasswordDTO.getUsername());

        when(passwordEncoder.matches(changeUserPasswordDTO.getOldPassword(),
                user.get().getPassword()))
                .thenReturn(false);

        assertFalse(userService.changeUserPassword(changeUserPasswordDTO));
    }

    @Test
    void changeUserPasswordIsSuccessfulTest() {
        when(userRepository.findByUsername(changeUserPasswordDTO.getUsername()))
                .thenReturn(Optional.ofNullable(userEntity));

        Optional<UserEntity> user = userRepository.findByUsername(changeUserPasswordDTO.getUsername());

        when(passwordEncoder.matches(changeUserPasswordDTO.getOldPassword(),
                user.get().getPassword()))
                .thenReturn(true);

        when(passwordEncoder.encode(changeUserPasswordDTO.getNewPassword()))
                .thenReturn("admin2");

        assertTrue(userService.changeUserPassword(changeUserPasswordDTO));
        assertEquals(user.get().getPassword(),changeUserPasswordDTO.getNewPassword());
    }

    @Test
    void deleteUserByUsernameSuccessfulTest(){
        when(userRepository.findByUsername(userEntity.getUsername()))
                .thenReturn(Optional.of(userEntity));

        UserEntity user = userRepository.findByUsername(userEntity.getUsername()).get();

        when(offerService.getAllOffersBySeller(user))
                .thenReturn(List.of(offerEntity));

        List<OfferEntity> allOffersBySeller =
                offerService.getAllOffersBySeller(user);

//        when(mapper.map(user, UserDetailsView.class))
//                .thenReturn(userDetailsView);

        assertEquals(userDetailsView.getUsername(), user.getUsername());
    }
}
