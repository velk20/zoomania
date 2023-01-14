package com.zoomania.zoomania.web;

import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.enums.CategoryEnum;
import com.zoomania.zoomania.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerIT {
    private final String BASE_URL ="http://localhost";
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
    void testGetAllOffers() throws Exception {
        mockMvc.perform(get("/offers/all")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    void testGetMyOffers_FoundButRedirectedToLoginPage() throws Exception {
        mockMvc.perform(get("/offers/my")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(USER_AUTHENTICATION_PAGE));
    }

    @Test
    @WithUserDetails(value = "testUser",
            userDetailsServiceBeanName = "testUserDataService")
    void testGetMyOffersWithUser() throws Exception {
        mockMvc.perform(get("/offers/my")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user-offers"))
                .andExpect(model().attributeExists("offers"));
    }

    @Test
    void testGetCreateOffer_Found() throws Exception {
        mockMvc.perform(get("/offers/create").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(USER_AUTHENTICATION_PAGE));
    }

    @Test
    @WithUserDetails(value = "testUser",
            userDetailsServiceBeanName = "testUserDataService")
    void testGetCreateOfferWithUser() throws Exception {
        mockMvc.perform(get("/offers/create")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("create-offer"))
                .andExpect(model().attributeExists("addOfferModel"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void testCreateOffer() throws Exception {
        mockMvc.perform(post("/offers/create")
                .param("title","MY dog")
                .param("breed","Husky")
                .param("price", String.valueOf(BigDecimal.valueOf(123)))
                .param("category", String.valueOf(CategoryEnum.Dogs))
                .param("imageUrl","http://image.com")
                .param("description","My description.")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/offers/all"));
    }

    @Test
    void testGetOfferDetails() throws  Exception {
        mockMvc.perform(get("/offers/1/details")
                .with(csrf()))
                .andExpect(model().attributeExists("offer"))
                .andExpect(model().attributeExists("offerSellerUsername"))
                .andExpect(view().name("details-offer"));
    }

    @Test
    void testGetOfferDetailsWithWrongId_NotFound() throws  Exception {
        mockMvc.perform(get("/offers/12/details")
                .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void testGetOfferEditPage() throws Exception {
        mockMvc.perform(get("/offers/1/edit")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-offer"));
    }
}
