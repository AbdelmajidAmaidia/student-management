package com.example.student.dto.course;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final Integer credits;
    private final String teacherName;
    private final String teacherEmail;
}
