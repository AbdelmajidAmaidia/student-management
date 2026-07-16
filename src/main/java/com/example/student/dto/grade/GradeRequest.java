package com.example.student.dto.grade;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeRequest {

    @NotNull
    private Long enrollmentId;

    @NotNull
    @Min(0)
    @Max(20)
    private Double value;

    @NotBlank
    private String semester;
}
