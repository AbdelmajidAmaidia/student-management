package com.example.student.service.impl;

import com.example.student.dto.enrollment.EnrollmentRequest;
import com.example.student.dto.enrollment.EnrollmentResponse;
import com.example.student.entity.Course;
import com.example.student.entity.Enrollment;
import com.example.student.entity.Student;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.EnrollmentMapper;
import com.example.student.repository.CourseRepository;
import com.example.student.repository.EnrollmentRepository;
import com.example.student.repository.StudentRepository;
import com.example.student.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public EnrollmentResponse create(EnrollmentRequest request) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(request.getStudentId(), request.getCourseId())) {
            throw new DuplicateResourceException("Enrollment already exists for student/course");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + request.getCourseId()));

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found: " + id));
        return enrollmentMapper.toResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAll() {
        return enrollmentRepository.findAll().stream().map(enrollmentMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found: " + id));
        enrollmentRepository.delete(enrollment);
    }
}
