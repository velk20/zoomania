package com.zoomania.zoomania.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoomania.zoomania.model.dto.comment.CommentCreationDTO;
import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.util.TestDataUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentRestControllerIT {
    @Autowired
    private ObjectMapper mapper;
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
    @WithMockUser(value = "testAdmin")
    void testGetAllUsersRest() throws Exception {
        mockMvc.perform(get(String.format("/api/%d/comments",testOffer.getId())))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "testAdmin",
            userDetailsServiceBeanName = "testUserDataService")
    void testCreateComment() throws Exception {
        CommentCreationDTO comment =
                new CommentCreationDTO(testAdmin.getUsername(),testOffer.getId(),"New comment");

        final String baseUrl =
                "http://localhost:8080/api/" + testOffer.getId() + "/comments";
        mockMvc.perform(post(baseUrl)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(comment)))
                .andExpect(status().isCreated());
    }
}
