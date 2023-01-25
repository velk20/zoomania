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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIT {
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
    void testGetAllAdminUserWithNotUser() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    void testGetAllAdminOffersWithNotUser() throws Exception {
        mockMvc.perform(get("/admin/offers")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithUserDetails(value = "testUser",
            userDetailsServiceBeanName = "testUserDataService")
    void testGetAllAdminUsersWithUserButNoAdmin() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "testUser",
            userDetailsServiceBeanName = "testUserDataService")
    void testGetAllAdminOffersWithUserButNoAdmin() throws Exception {
        mockMvc.perform(get("/admin/offers")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "testAdmin",roles = {"ADMIN"})
    void testGetAllAdminUsersWithUserAdmin() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-users"));
    }

    @Test
    @WithMockUser(value = "testAdmin",roles = {"ADMIN"})
    void testGetAllAdminOffersWithUserAdmin() throws Exception {
        mockMvc.perform(get("/admin/offers")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-offers"));
    }
}
