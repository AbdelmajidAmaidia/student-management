package com.example.student.service;

import com.example.student.dto.auth.LoginRequest;
import com.example.student.dto.auth.RefreshTokenRequest;
import com.example.student.dto.auth.RegisterRequest;
import com.example.student.entity.AppUser;
import com.example.student.entity.RefreshToken;
import com.example.student.entity.Role;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.UnauthorizedException;
import com.example.student.repository.AppUserRepository;
import com.example.student.repository.RefreshTokenRepository;
import com.example.student.security.JwtService;
import com.example.student.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AppUserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("admin@example.com");
        registerRequest.setPassword("secret");
        registerRequest.setRole(Role.ADMIN);
    }

    @Test
    void shouldRegisterUser() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateAccessToken(any())).thenReturn("access");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh");

        assertNotNull(authService.register(registerRequest));
    }

    @Test
    void shouldRejectDuplicateRegistration() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.register(registerRequest));
    }

    @Test
    void shouldLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@example.com");
        loginRequest.setPassword("secret");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(AppUser.builder().email("admin@example.com").password("p").role(Role.ADMIN).build()));
        when(jwtService.generateAccessToken(any())).thenReturn("access");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh");

        assertNotNull(authService.login(loginRequest));
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void shouldRefreshToken() {
        AppUser user = AppUser.builder().email("admin@example.com").password("x").role(Role.ADMIN).build();
        RefreshToken token = RefreshToken.builder().token("r").user(user).expiresAt(LocalDateTime.now().plusDays(1)).build();
        when(refreshTokenRepository.findByToken("r")).thenReturn(Optional.of(token));
        when(jwtService.generateAccessToken(any())).thenReturn("new-access");

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("r");

        assertNotNull(authService.refresh(request));
    }

    @Test
    void shouldThrowWhenRefreshExpired() {
        AppUser user = AppUser.builder().email("admin@example.com").password("x").role(Role.ADMIN).build();
        RefreshToken token = RefreshToken.builder().token("r").user(user).expiresAt(LocalDateTime.now().minusMinutes(1)).build();
        when(refreshTokenRepository.findByToken("r")).thenReturn(Optional.of(token));

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("r");

        assertThrows(UnauthorizedException.class, () -> authService.refresh(request));
    }
}
