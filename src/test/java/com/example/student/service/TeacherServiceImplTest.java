package com.example.student.service;

import com.example.student.dto.teacher.TeacherRequest;
import com.example.student.entity.Teacher;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.TeacherMapper;
import com.example.student.repository.AppUserRepository;
import com.example.student.repository.TeacherRepository;
import com.example.student.service.impl.TeacherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private TeacherMapper teacherMapper;
    @Mock
    private AppUserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private TeacherServiceImpl teacherService;

    private TeacherRequest request;

    @BeforeEach
    void setUp() {
        request = new TeacherRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
    }

    @Test
    void shouldCreateTeacher() {
        when(teacherRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(teacherRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("x");
        when(teacherMapper.toResponse(any())).thenReturn(null);

        assertDoesNotThrow(() -> teacherService.create(request));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        when(teacherRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> teacherService.create(request));
    }

    @Test
    void shouldUpdateTeacher() {
        Teacher teacher = Teacher.builder().id(1L).email("old@example.com").build();
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(teacherMapper.toResponse(any())).thenReturn(null);

        assertDoesNotThrow(() -> teacherService.update(1L, request));
    }

    @Test
    void shouldThrowWhenTeacherNotFound() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> teacherService.update(1L, request));
    }

    @Test
    void shouldDeleteTeacher() {
        Teacher teacher = Teacher.builder().id(1L).email("x@y.com").build();
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        assertDoesNotThrow(() -> teacherService.delete(1L));
    }
}
