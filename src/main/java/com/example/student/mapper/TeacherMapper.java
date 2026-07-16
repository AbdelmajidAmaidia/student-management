package com.example.student.mapper;

import com.example.student.dto.teacher.TeacherResponse;
import com.example.student.entity.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {
    public TeacherResponse toResponse(Teacher teacher) {
        return TeacherResponse.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .specialty(teacher.getSpecialty())
                .build();
    }
}
