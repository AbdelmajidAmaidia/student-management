package com.example.student.service;

import com.example.student.dto.enrollment.EnrollmentRequest;
import com.example.student.entity.Course;
import com.example.student.entity.Enrollment;
import com.example.student.entity.Student;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.EnrollmentMapper;
import com.example.student.repository.CourseRepository;
import com.example.student.repository.EnrollmentRepository;
import com.example.student.repository.StudentRepository;
import com.example.student.service.impl.EnrollmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private EnrollmentMapper enrollmentMapper;
    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private EnrollmentRequest request;

    @BeforeEach
    void setUp() {
        request = new EnrollmentRequest();
        request.setStudentId(1L);
        request.setCourseId(2L);
    }

    @Test
    void shouldCreateEnrollment() {
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(false);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(Student.builder().id(1L).build()));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(Course.builder().id(2L).build()));
        when(enrollmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(enrollmentMapper.toResponse(any())).thenReturn(null);

        assertDoesNotThrow(() -> enrollmentService.create(request));
    }

    @Test
    void shouldRejectDuplicateEnrollment() {
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> enrollmentService.create(request));
    }

    @Test
    void shouldGetById() {
        Enrollment enrollment = Enrollment.builder().id(1L).student(Student.builder().id(1L).build()).course(Course.builder().id(2L).build()).build();
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentMapper.toResponse(any())).thenReturn(null);

        assertDoesNotThrow(() -> enrollmentService.getById(1L));
    }

    @Test
    void shouldThrowOnDeleteMissingEnrollment() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> enrollmentService.delete(1L));
    }
}
