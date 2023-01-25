package com.zoomania.zoomania.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminRestControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(value = "testAdmin",roles = {"ADMIN"})
    void testGetAllUsersRest() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testAdmin",roles = {"ADMIN"})
    void testGetAllOffersRest() throws Exception {
        mockMvc.perform(get("/api/offers"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testAdmin",roles = {"USER"})
    void testGetAllUsersRest_AccessDenied() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "testAdmin",roles = {"USER"})
    void testGetAllOffersRest_AccessDenied() throws Exception {
        mockMvc.perform(get("/api/offers"))
                .andExpect(status().isForbidden());
    }
}
