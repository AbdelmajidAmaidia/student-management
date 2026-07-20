package com.example.student.controller;

import com.example.student.dto.auth.AuthResponse;
import com.example.student.dto.auth.LoginRequest;
import com.example.student.dto.auth.RefreshTokenRequest;
import com.example.student.dto.auth.RegisterRequest;
import com.example.student.entity.Role;
import com.example.student.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void shouldRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("admin@example.com");
        request.setPassword("secret123");
        request.setRole(Role.ADMIN);

        when(authService.register(any())).thenReturn(AuthResponse.builder().accessToken("a").refreshToken("r").tokenType("Bearer").email("e").role("ADMIN").build());

        ResponseEntity<AuthResponse> response = authController.register(request);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldLogin() {
        LoginRequest request = new LoginRequest();
        request.setEmail("admin@example.com");
        request.setPassword("secret123");

        when(authService.login(any())).thenReturn(AuthResponse.builder().accessToken("a").refreshToken("r").tokenType("Bearer").email("e").role("ADMIN").build());

        ResponseEntity<AuthResponse> response = authController.login(request);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldRefresh() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("r");

        when(authService.refresh(any())).thenReturn(AuthResponse.builder().accessToken("a").refreshToken("r").tokenType("Bearer").email("e").role("ADMIN").build());

        ResponseEntity<AuthResponse> response = authController.refresh(request);
        assertEquals(200, response.getStatusCode().value());
    }
}
