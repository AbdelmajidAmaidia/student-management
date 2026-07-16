package com.example.student.mapper;

import com.example.student.dto.student.StudentResponse;
import com.example.student.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .birthDate(student.getBirthDate())
                .gender(student.getGender())
                .address(student.getAddress())
                .department(student.getDepartment() != null ? student.getDepartment().getName() : null)
                .enrollmentDate(student.getEnrollmentDate())
                .status(student.getStatus())
                .build();
    }
}
