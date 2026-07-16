package com.example.student.dto.teacher;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeacherResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Long departmentId;
    private String departmentName;
}
