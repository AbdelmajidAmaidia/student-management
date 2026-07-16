package com.example.student.dto.teacher;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeacherResponse {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String specialty;
}
