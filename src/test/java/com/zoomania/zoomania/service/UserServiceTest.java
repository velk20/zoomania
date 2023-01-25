package com.zoomania.zoomania.service;

import com.zoomania.zoomania.model.dto.user.UserRegisterDTO;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.entity.UserRoleEntity;
import com.zoomania.zoomania.model.enums.UserRoleEnum;
import com.zoomania.zoomania.repository.CategoryRepository;
import com.zoomania.zoomania.repository.CommentRepository;
import com.zoomania.zoomania.repository.UserRepository;
import com.zoomania.zoomania.repository.UserRoleRepository;
import com.zoomania.zoomania.util.TestDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class UserServiceTest {
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public  static final int DEFAULT_PAGE_SIZE = 8;
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private ModelMapper mapper;
    @Mock
    private OfferService offerService;

    @InjectMocks
    private UserService userService;

    private UserRegisterDTO userRegisterDTO;
    private UserEntity user;
    private UserEntity admin;
    private UserRoleEntity roleEntityAdmin;
    private UserRoleEntity roleEntityUser;
    @BeforeEach
    void setUp() {
        this.roleEntityUser = new UserRoleEntity().setUserRoleEnum(UserRoleEnum.ADMIN);
        this.userDetailsService = new ZooManiaUserDetailsService(userRepository);
        userRegisterDTO = new UserRegisterDTO()
                .setAge(21)
                .setEmail("email@email.com")
                .setUsername("admin")
                .setFirstName("Admin")
                .setLastName("Adminov")
                .setPhone("546983312")
                .setPassword("passs")
                .setConfirmPassword("passs");
        user = new UserEntity().setUsername("admin");
    }

//    @Test
//    void testRegisterAndLoginUser(){
//        when(userRoleRepository.findByUserRoleEnum(UserRoleEnum.USER))
//                .thenReturn(roleEntityUser);
//        when(mapper.map(userRegisterDTO, UserEntity.class))
//                .thenReturn(user);
//        when(userRepository.save(user))
//                .thenReturn(user);
//        when(userRepository.findByUsername(userRegisterDTO.getUsername()))
//                .thenReturn(Optional.of(user));
//        this.userService.registerAndLogin(userRegisterDTO);
//
//        Assertions.assertTrue(userRepository.findByUsername(user.getUsername()).isPresent());
//
//    }
}
