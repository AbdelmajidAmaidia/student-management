package com.example.student.dto.course;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private Integer credits;
    private Long teacherId;
    private String teacherName;
}
