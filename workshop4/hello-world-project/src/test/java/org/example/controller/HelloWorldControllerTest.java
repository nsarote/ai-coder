package org.example.controller;

import org.example.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloWorldController.class)
class HelloWorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloWorld_shouldReturnMessage() throws Exception {
        mockMvc.perform(get("/hello-world"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("helloworld"));
    }

    @Test
    void register_shouldReturnSuccessMessage() throws Exception {
        String requestBody = "{\"email\":\"user@example.com\",\"password\":\"yourpassword\"}";
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void login_shouldReturnTokenOnValidCredentials() throws Exception {
        String requestBody = "{\"email\":\"user@example.com\",\"password\":\"yourpassword\"}";
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_shouldReturnErrorOnInvalidCredentials() throws Exception {
        String requestBody = "{\"email\":\"wrong@example.com\",\"password\":\"wrongpass\"}";
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    void getMe_shouldReturnUserInfoOnValidToken() throws Exception {
        // Assume JwtUtil.generateToken("user@example.com") returns "valid-token"
        String token = JwtUtil.generateToken("user@example.com");
        mockMvc.perform(get("/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));
    }

    @Test
    void getMe_shouldReturnErrorOnInvalidToken() throws Exception {
        mockMvc.perform(get("/me").header("Authorization", "Bearer xxx"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid token"));
    }

    @Test
    void getMe_shouldReturnErrorOnMissingAuthorization() throws Exception {
        mockMvc.perform(get("/me"))
                .andExpect(status().isBadRequest());
    }
}