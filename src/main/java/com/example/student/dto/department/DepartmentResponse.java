package com.example.student.dto.department;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepartmentResponse {
    private final Long id;
    private final String name;
    private final String description;
}
