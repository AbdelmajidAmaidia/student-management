package com.example.student.dto;

import com.example.student.entity.Student;

public record StudentResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Integer age
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(student.getId(), student.getFirstName(), student.getLastName(), student.getEmail(), student.getAge());
    }
}
