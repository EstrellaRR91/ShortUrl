package com.shorturl.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shorturl.model.User;
import com.shorturl.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Para serializar/deserializar JSON

    @Autowired
    private UserRepository userRepository;

    @Test
    void testUserRegistration() throws Exception {
        
        // Creamos un objeto user para registro
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("newuser"));
        
        // Aquí puedes agregar más asserts según la respuesta que esperes
    }

    // También puedes agregar tests para login, errores, etc.
}