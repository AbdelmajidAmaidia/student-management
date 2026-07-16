package com.example.student.service;

import com.example.student.dto.student.StudentRequest;
import com.example.student.entity.*;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.StudentMapper;
import com.example.student.repository.AppUserRepository;
import com.example.student.repository.DepartmentRepository;
import com.example.student.repository.StudentRepository;
import com.example.student.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private AppUserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private StudentServiceImpl studentService;

    private StudentRequest request;

    @BeforeEach
    void setUp() {
        request = new StudentRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("jane@example.com");
        request.setBirthDate(LocalDate.now().minusYears(20));
        request.setGender(Gender.FEMALE);
        request.setDepartmentId(1L);
        request.setEnrollmentDate(LocalDate.now());
        request.setStatus(StudentStatus.ACTIVE);
    }

    @Test
    void shouldCreateStudent() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(Department.builder().id(1L).name("CS").build()));
        when(studentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("x");
        when(studentMapper.toResponse(any())).thenReturn(null);

        assertDoesNotThrow(() -> studentService.create(request));
    }

    @Test
    void shouldRejectDuplicateEmail() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> studentService.create(request));
    }

    @Test
    void shouldThrowWhenDepartmentNotFound() {
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.create(request));
    }

    @Test
    void shouldUpdateStudent() {
        Student student = Student.builder().id(2L).email("old@example.com").build();
        when(studentRepository.findById(2L)).thenReturn(Optional.of(student));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(Department.builder().id(1L).build()));
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentMapper.toResponse(any())).thenReturn(null);

        assertDoesNotThrow(() -> studentService.update(2L, request));
    }

    @Test
    void shouldGetCurrentStudent() {
        when(studentRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(Student.builder().id(1L).build()));
        when(studentMapper.toResponse(any())).thenReturn(null);

        assertDoesNotThrow(() -> studentService.getCurrentStudent("jane@example.com"));
    }

    @Test
    void shouldThrowWhenStudentNotFoundOnDelete() {
        when(studentRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.delete(4L));
    }
}
