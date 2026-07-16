package com.example.student.dto.student;

import com.example.student.entity.Gender;
import com.example.student.entity.StudentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class StudentResponse {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final LocalDate birthDate;
    private final Gender gender;
    private final String address;
    private final String department;
    private final LocalDate enrollmentDate;
    private final StudentStatus status;
}
