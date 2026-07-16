package com.example.student.dto.department;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
}
