package com.example.student.service.impl;

import com.example.student.dto.grade.GradeRequest;
import com.example.student.dto.grade.GradeResponse;
import com.example.student.entity.Enrollment;
import com.example.student.entity.Grade;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.GradeMapper;
import com.example.student.repository.EnrollmentRepository;
import com.example.student.repository.GradeRepository;
import com.example.student.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeMapper gradeMapper;

    @Override
    public GradeResponse createOrUpdate(GradeRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found: " + request.getEnrollmentId()));

        Grade grade = gradeRepository.findByEnrollmentId(request.getEnrollmentId())
                .orElseGet(() -> Grade.builder().enrollment(enrollment).build());

        grade.setValue(request.getValue());
        grade.setSemester(request.getSemester());

        Grade saved = gradeRepository.save(grade);
        enrollment.setGrade(saved);

        return gradeMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GradeResponse getByEnrollment(Long enrollmentId) {
        Grade grade = gradeRepository.findByEnrollmentId(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found for enrollment: " + enrollmentId));
        return gradeMapper.toResponse(grade);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeResponse> getAll() {
        return gradeRepository.findAll().stream().map(gradeMapper::toResponse).toList();
    }
}
