package com.shorturl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shorturl.model.User;
import com.shorturl.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; 

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
    }

    @Test
    void testUserRegistration() throws Exception {
        
        
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("newuser"))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void testDuplicateUserRegistration() throws Exception{
        User user = new User();
        user.setUsername("duplicateruser");
        user.setPassword("password123");

        userRepository.save(user);

        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isBadRequest());
        
    }
    }

    