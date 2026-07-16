package com.example.student.dto.student;

import com.example.student.entity.Gender;
import com.example.student.entity.StudentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Gender gender;

    @NotBlank
    private String address;

    @NotNull
    private Long departmentId;

    @NotNull
    private LocalDate enrollmentDate;

    @NotNull
    private StudentStatus status;
}
