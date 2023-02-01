package com.zoomania.zoomania.repository;

import com.zoomania.zoomania.model.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    private static final String TEST_USERNAME = "admin";
    @Autowired
    private UserRepository userRepository;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        this.testUser = new UserEntity()
                .setUsername(TEST_USERNAME)
                .setEmail(TEST_USERNAME + "@gmail.com")
                .setPassword("strongPassword")
                .setPhone("6565656");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void itShouldCheckIfUserExistWithUsername() {
        UserEntity save = userRepository.save(testUser);

        Assertions.assertTrue(this.userRepository.findByUsername(save.getUsername()).isPresent());
    }

    @Test
    void itShouldCheckIfUserExistWithUsernameThatDoesNotExists() {
        Assertions.assertFalse(this.userRepository.findByUsername(testUser.getUsername()).isPresent());
    }
}
