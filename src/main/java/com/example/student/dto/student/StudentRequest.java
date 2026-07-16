package com.example.student.dto.student;

import com.example.student.entity.Gender;
import com.example.student.entity.StudentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    private String phone;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Gender gender;

    private String address;

    @NotNull
    private Long departmentId;

    @NotNull
    private LocalDate enrollmentDate;

    @NotNull
    private StudentStatus status;
}
