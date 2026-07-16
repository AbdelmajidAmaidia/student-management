package com.example.student.dto.grade;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GradeResponse {
    private final Long id;
    private final Long enrollmentId;
    private final Double value;
    private final String semester;
}
