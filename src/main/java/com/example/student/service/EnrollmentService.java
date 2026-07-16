package com.example.student.service;

import com.example.student.dto.enrollment.EnrollmentRequest;
import com.example.student.dto.enrollment.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse create(EnrollmentRequest request);
    EnrollmentResponse getById(Long id);
    List<EnrollmentResponse> getAll();
    void delete(Long id);
}
