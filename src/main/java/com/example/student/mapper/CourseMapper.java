package com.example.student.mapper;

import com.example.student.dto.course.CourseResponse;
import com.example.student.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    public CourseResponse toResponse(Course course) {
        String teacherName = null;
        String teacherEmail = null;
        if (course.getTeacher() != null) {
            teacherName = course.getTeacher().getFirstName() + " " + course.getTeacher().getLastName();
            teacherEmail = course.getTeacher().getEmail();
        }

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .credits(course.getCredits())
                .teacherName(teacherName)
                .teacherEmail(teacherEmail)
                .build();
    }
}
