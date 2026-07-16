package com.example.student.mapper;

import com.example.student.dto.grade.GradeResponse;
import com.example.student.entity.Grade;
import org.springframework.stereotype.Component;

@Component
public class GradeMapper {
    public GradeResponse toResponse(Grade grade) {
        return GradeResponse.builder()
                .id(grade.getId())
                .enrollmentId(grade.getEnrollment().getId())
                .value(grade.getValue())
                .semester(grade.getSemester())
                .build();
    }
}
