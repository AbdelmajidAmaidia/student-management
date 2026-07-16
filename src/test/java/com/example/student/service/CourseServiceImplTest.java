package com.example.student.service;

import com.example.student.dto.course.CourseRequest;
import com.example.student.entity.Teacher;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.CourseMapper;
import com.example.student.repository.CourseRepository;
import com.example.student.repository.TeacherRepository;
import com.example.student.service.impl.CourseServiceImpl;
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
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private CourseMapper courseMapper;
    @InjectMocks
    private CourseServiceImpl courseService;

    private CourseRequest request;

    @BeforeEach
    void setUp() {
        request = new CourseRequest();
        request.setTitle("Algorithms");
        request.setCredits(4);
        request.setTeacherId(1L);
    }

    @Test
    void shouldCreateCourse() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(Teacher.builder().id(1L).build()));
        when(courseRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(courseMapper.toResponse(any())).thenReturn(null);

        assertDoesNotThrow(() -> courseService.create(request));
    }

    @Test
    void shouldThrowIfTeacherMissingOnCreate() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.create(request));
    }

    @Test
    void shouldUpdateCourse() {
        when(courseRepository.findById(2L)).thenReturn(Optional.of(com.example.student.entity.Course.builder().id(2L).build()));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(Teacher.builder().id(1L).build()));
        when(courseMapper.toResponse(any())).thenReturn(null);

        assertDoesNotThrow(() -> courseService.update(2L, request));
    }

    @Test
    void shouldDeleteCourse() {
        when(courseRepository.findById(2L)).thenReturn(Optional.of(com.example.student.entity.Course.builder().id(2L).build()));

        assertDoesNotThrow(() -> courseService.delete(2L));
    }
}
