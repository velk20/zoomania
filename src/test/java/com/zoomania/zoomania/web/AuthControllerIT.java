package com.zoomania.zoomania.web;

import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
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
public class AuthControllerIT {
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
        void getLoginPage() throws Exception {
                mockMvc.perform(get("/users/login"))
                        .andExpect(view().name("login"));
        }

        @Test
        @WithUserDetails(value = "testAdmin",
                userDetailsServiceBeanName = "testUserDataService")
                void getLoginPageWhenUserIsLoggedIn() throws Exception {
                mockMvc.perform(get("/users/login"))
                        .andExpect(status().isForbidden());

        }

        @Test
        void getRegisterPage() throws Exception {
                mockMvc.perform(get("/users/register"))
                        .andExpect(view().name("register"));
        }

        @Test
        @WithUserDetails(value = "testAdmin",
                userDetailsServiceBeanName = "testUserDataService")
        void getRegisterPageWhenUserIsLoggedIn() throws Exception {
                mockMvc.perform(get("/users/register"))
                        .andExpect(status().isForbidden());

        }

        @Test
        void registerUserTest() throws Exception {
                mockMvc.perform(post("/users/register")
                                .param("username","admin2")
                                .param("firstName","admin2")
                                .param("email","admin2@admin.com")
                                .param("lastName","admin2")
                                .param("phone","05155556")
                                .param("age","22")
                                .param("password","admin2")
                                .param("confirmPassword","admin2")
                                .with(csrf())
                        )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/"));

        }

        @Test
        void registerUserTest_InvalidParams() throws Exception {
                mockMvc.perform(post("/users/register")
                                .param("username","ad")
                                .param("firstName","ad")
                                .param("email","admin2@admin.com")
                                .param("lastName","admin2")
                                .param("phone","05155556")
                                .param("age","22")
                                .param("password","admin2")
                                .param("confirmPassword","admin232")
                                .with(csrf())
                        )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/users/register"))
                        .andExpect(flash().attributeExists("userModel"));

        }

        @Test
        void testOnLoginFailed() throws Exception {
                mockMvc.perform(post("/users/login-error")
                        .param("username",testAdmin.getUsername())
                        .param("password",testAdmin.getPassword()+1)
                        .with(csrf()))
                        .andExpect(redirectedUrl("http://localhost/users/login"));
        }
}
