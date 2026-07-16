package com.example.student.service.impl;

import com.example.student.dto.auth.*;
import com.example.student.entity.AppUser;
import com.example.student.entity.RefreshToken;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.UnauthorizedException;
import com.example.student.repository.AppUserRepository;
import com.example.student.repository.RefreshTokenRepository;
import com.example.student.security.JwtService;
import com.example.student.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already used");
        }

        AppUser user = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);

        UserDetails userDetails = toUserDetails(user);
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = createRefreshToken(user, userDetails);

        return buildResponse(user, accessToken, refreshToken);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        AppUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        UserDetails userDetails = toUserDetails(user);
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = createRefreshToken(user, userDetails);

        return buildResponse(user, accessToken, refreshToken);
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Refresh token expired");
        }

        AppUser user = refreshToken.getUser();
        UserDetails userDetails = toUserDetails(user);
        String accessToken = jwtService.generateAccessToken(userDetails);

        return buildResponse(user, accessToken, refreshToken.getToken());
    }

    private String createRefreshToken(AppUser user, UserDetails userDetails) {
        refreshTokenRepository.deleteByUser(user);

        String refreshTokenValue = jwtService.generateRefreshToken(userDetails);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(refreshToken);

        return refreshTokenValue;
    }

    private UserDetails toUserDetails(AppUser user) {
        return new User(user.getEmail(), user.getPassword(),
                List.of(() -> "ROLE_" + user.getRole().name()));
    }

    private AuthResponse buildResponse(AppUser user, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
