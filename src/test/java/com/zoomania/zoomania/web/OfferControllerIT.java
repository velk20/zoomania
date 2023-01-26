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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerIT {
    private final String IMAGE_PATH = "src/main/resources/static/images/fish/angelfish/angelfish.jpg";
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
    void testCreateOffer_InvalidImage() throws Exception {
        mockMvc.perform(post("/offers/create")
                        .param("title", "MY dog")
                        .param("breed", "Husky")
                        .param("price", String.valueOf(BigDecimal.valueOf(123)))
                        .param("category", String.valueOf(CategoryEnum.Dogs))
                        .param("description", "My description.")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("error"));
    }

    @Test
    void testGetOfferDetails() throws Exception {
        mockMvc.perform(get(String.format("/offers/%d/details", this.testOffer.getId()))
                        .with(csrf()))
                .andExpect(model().attributeExists("offer"))
                .andExpect(model().attributeExists("offerSellerUsername"))
                .andExpect(view().name("details-offer"));
    }

    @Test
    void testGetOfferDetailsWithWrongId_NotFound() throws Exception {
        mockMvc.perform(get(String.format("/offers/%d/details", testOffer.getId() + 100))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void testGetOfferEditPage() throws Exception {
        mockMvc.perform(get(String.format("/offers/%d/edit", testAdminOffer.getId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-offer"));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void testGetOfferEditPageWithInvalidId_NotFound() throws Exception {
        mockMvc.perform(get(String.format("/offers/%d/edit", testOffer.getId() + 100))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void testEditOfferWithInvalidParams_Error() throws Exception {
        mockMvc.perform(patch(String.format("/offers/%d/edit", testOffer.getId()))
                        .param("title", "MY new dog")
                        .param("breed", "Husky 2")
                        .param("images", "iamges")
                        .param("price", String.valueOf(BigDecimal.valueOf(123)))
                        .param("category", String.valueOf(CategoryEnum.Dogs))
                        .param("description", "My description.")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("error"));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void testSearchOfferPage() throws Exception {
        mockMvc.perform(get("/offers/search")
                .param("minPrice","100")
                .with(csrf()))
                .andExpect(view().name("search-offer"))
                .andExpect(model().attributeExists("offers"));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void testSearchOfferPage_InvalidParams() throws Exception {
        mockMvc.perform(get("/offers/search")
                        .param("minPrice","-1")
                        .with(csrf()))
                .andExpect(view().name("search-offer"))
                .andExpect(model().attributeDoesNotExist("offers"))
                .andExpect(model().attributeExists("searchOfferModel"));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void testDeleteOffer() throws Exception {
        mockMvc.perform(
                        delete(String.format("/offers/%d/delete",
                                testAdminOffer.getId()))
                                .with(csrf()))
                .andExpect(redirectedUrl("/offers/all"));
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void testDeleteOffer_ThrowsOfferNotFound() throws Exception {
        mockMvc.perform(
                        delete(String.format("/offers/%d/delete",
                                testAdminOffer.getId()-100))
                                .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("message"));
    }
}
