package com.example.student.dto.student;

import com.example.student.entity.Gender;
import com.example.student.entity.StudentStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private Long departmentId;
    private String departmentName;
    private LocalDate enrollmentDate;
    private StudentStatus status;
}
