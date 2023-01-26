package com.zoomania.zoomania.web;

import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerIT {
    private final String BASE_URL = "http://localhost";
    private final String USER_AUTHENTICATION_PAGE = BASE_URL + "/users/login";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestDataUtils testDataUtils;

    private UserEntity testUser, testAdmin;

    private OfferEntity testOffer, testAdminOffer;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("testUser");
        testAdmin = testDataUtils.createTestAdmin("testAdmin");

        List<CategoryEntity> categoryEntities = testDataUtils.initCategory();

        testOffer = testDataUtils.createTestOffer(testUser, categoryEntities.get(0));
        testAdminOffer = testDataUtils.createTestOffer(testAdmin, categoryEntities.get(1));
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void getProfilePageWithoutLogin_RedirectTOLoginPage() throws Exception {
        mockMvc.perform(get("/users/profile/" + testAdmin.getUsername())
                        .with(csrf()))
                .andExpect(redirectedUrl(USER_AUTHENTICATION_PAGE));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void getProfilePageWithUser() throws Exception {
        mockMvc.perform(get("/users/profile/" + testAdmin.getUsername())
                        .with(csrf()))
                .andExpect(view().name("profile"));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void updateUserProfile() throws Exception {
        String newUsername = "userNewUsername";
        mockMvc.perform(patch(String.format("/users/profile/%s/edit", testAdmin.getUsername()))
                        .param("username",newUsername)
                        .param("firstName",testAdmin.getFirstName())
                        .param("email",testAdmin.getEmail())
                        .param("lastName",testAdmin.getLastName())
                        .param("phone",testAdmin.getPhone())
                        .param("age", String.valueOf(testAdmin.getAge()))
                        .param("active","true")
                        .param("admin","true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile/"+newUsername));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void updateUserProfile_WithInvalidParams() throws Exception {
        String newUsername = "userNewUsername";
        mockMvc.perform(patch(String.format("/users/profile/%s/edit", testAdmin.getUsername()))
                        .param("username",newUsername)
                        .param("firstName","a")
                        .param("email",testAdmin.getEmail())
                        .param("lastName",testAdmin.getLastName())
                        .param("phone",testAdmin.getPhone())
                        .param("age", String.valueOf(testAdmin.getAge()))
                        .param("active","true")
                        .param("admin","true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("user"))
                .andExpect(redirectedUrl("/users/profile/"+testAdmin.getUsername()));
    }

    @Test
    @WithUserDetails(value = "testUser",
            userDetailsServiceBeanName = "testUserDataService")
    void getProfileChangePassword_AccessDenied() throws Exception {
        mockMvc.perform(get(String.format("/users/profile/%s/change_password", testAdmin.getUsername())))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));

    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void getProfileChangePassword() throws Exception {
        mockMvc.perform(get(String.format("/users/profile/%s/change_password", testAdmin.getUsername())))
                .andExpect(status().isOk())
                .andExpect(view().name("user-change-password"));

    }


    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void patchProfileChangePassword() throws Exception {
        mockMvc.perform(patch(String.format("/users/profile/%s/change_password", testAdmin.getUsername()))
                        .param("username", testAdmin.getUsername())
                        .param("oldPassword", "admin")
                        .param("newPassword", "12345")
                        .param("confirmPassword", "12345")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile/" + testAdmin.getUsername()));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void patchProfileChangePassword_WithInvalidNewPasswords() throws Exception {
        mockMvc.perform(patch(String.format("/users/profile/%s/change_password", testAdmin.getUsername()))
                        .param("username", testAdmin.getUsername())
                        .param("oldPassword", "admin")
                        .param("newPassword", "1")
                        .param("confirmPassword", "12")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("user"))
                .andExpect(redirectedUrl(String.format("/users/profile/%s/change_password", testAdmin.getUsername())));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void patchProfileChangePassword_WrongOldPassword() throws Exception {
        mockMvc.perform(patch(String.format("/users/profile/%s/change_password", testAdmin.getUsername()))
                        .param("username", testAdmin.getUsername())
                        .param("oldPassword", "adminnnn")
                        .param("newPassword", "12345")
                        .param("confirmPassword", "12345")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("user"))
                .andExpect(flash().attributeExists("bad_credentials"))
                .andExpect(redirectedUrl(String.format("/users/profile/%s/change_password", testAdmin.getUsername())));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void deleteUserProfile() throws Exception {
        mockMvc.perform(delete(String.format("/users/profile/%s/delete", testUser.getUsername()))
                        .with(csrf()))
                .andExpect(redirectedUrl("/admin/users"));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void deleteUserProfileItself() throws Exception {
        mockMvc.perform(delete(String.format("/users/profile/%s/delete", testAdmin.getUsername()))
                        .with(csrf()))
                .andExpect(view().name("index"));

    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void deleteUserProfile_InvalidUsername() throws Exception {
        mockMvc.perform(delete(String.format("/users/profile/%s/delete", testAdmin.getUsername()+1))
                        .with(csrf()))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(status().isNotFound());

    }
}
