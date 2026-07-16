package com.example.student.service;

import com.example.student.dto.auth.AuthResponse;
import com.example.student.dto.auth.LoginRequest;
import com.example.student.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
