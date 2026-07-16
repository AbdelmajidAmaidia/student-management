package com.example.student.dto.enrollment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnrollmentResponse {
    private final Long id;
    private final Long studentId;
    private final String studentName;
    private final Long courseId;
    private final String courseTitle;
    private final Double grade;
    private final String semester;
}
