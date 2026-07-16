package com.example.student.mapper;

import com.example.student.dto.enrollment.EnrollmentResponse;
import com.example.student.entity.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {
    public EnrollmentResponse toResponse(Enrollment enrollment) {
        String studentName = enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName();
        Double gradeValue = enrollment.getGrade() != null ? enrollment.getGrade().getValue() : null;
        String semester = enrollment.getGrade() != null ? enrollment.getGrade().getSemester() : null;

        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentName(studentName)
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getTitle())
                .grade(gradeValue)
                .semester(semester)
                .build();
    }
}
