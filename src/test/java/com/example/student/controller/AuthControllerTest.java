package com.example.student.controller;

import com.example.student.dto.auth.AuthResponse;
import com.example.student.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegister() throws Exception {
        when(authService.register(any())).thenReturn(AuthResponse.builder().accessToken("a").refreshToken("r").tokenType("Bearer").email("e").role("ADMIN").build());
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of("email", "admin@example.com", "password", "secret123", "role", "ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldLogin() throws Exception {
        when(authService.login(any())).thenReturn(AuthResponse.builder().accessToken("a").refreshToken("r").tokenType("Bearer").email("e").role("ADMIN").build());
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of("email", "admin@example.com", "password", "secret123"))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRefresh() throws Exception {
        when(authService.refresh(any())).thenReturn(AuthResponse.builder().accessToken("a").refreshToken("r").tokenType("Bearer").email("e").role("ADMIN").build());
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of("refreshToken", "r"))))
                .andExpect(status().isOk());
    }
}
