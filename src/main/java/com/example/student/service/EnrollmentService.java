package com.example.student.service;

import com.example.student.dto.enrollment.EnrollmentRequest;
import com.example.student.dto.enrollment.EnrollmentResponse;
import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse create(EnrollmentRequest request);
    EnrollmentResponse getById(Long id);
    List<EnrollmentResponse> getAll();
    List<EnrollmentResponse> getByStudentId(Long studentId);
    EnrollmentResponse update(Long id, EnrollmentRequest request);
    void delete(Long id);
}
