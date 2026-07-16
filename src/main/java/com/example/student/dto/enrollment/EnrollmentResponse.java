package com.example.student.dto.enrollment;

import com.example.student.entity.Semester;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnrollmentResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseTitle;
    private LocalDate enrollmentDate;
    private Double grade;
    private Semester semester;
}
