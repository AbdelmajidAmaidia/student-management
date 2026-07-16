package com.example.student.dto.enrollment;

import com.example.student.entity.Semester;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

    @NotNull
    private LocalDate enrollmentDate;

    @NotNull
    @Min(0)
    @Max(20)
    private Double grade;

    @NotNull
    private Semester semester;
}
