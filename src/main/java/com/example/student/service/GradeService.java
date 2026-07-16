package com.example.student.service;

import com.example.student.dto.grade.GradeRequest;
import com.example.student.dto.grade.GradeResponse;

import java.util.List;

public interface GradeService {
    GradeResponse createOrUpdate(GradeRequest request);
    GradeResponse getByEnrollment(Long enrollmentId);
    List<GradeResponse> getAll();
}
