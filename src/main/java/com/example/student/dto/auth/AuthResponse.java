package com.example.student.dto.auth;

import com.example.student.entity.RoleName;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String accessToken;
    private String tokenType;
    private String username;
    private Set<RoleName> roles;
}
