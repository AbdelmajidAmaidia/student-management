package com.example.student;

import com.example.student.dto.student.StudentRequest;
import com.example.student.dto.student.StudentResponse;
import com.example.student.entity.Department;
import com.example.student.entity.Student;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.StudentMapper;
import com.example.student.repository.AppUserRepository;
import com.example.student.repository.DepartmentRepository;
import com.example.student.repository.StudentRepository;
import com.example.student.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

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

    private StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        studentService = new StudentServiceImpl(studentRepository, departmentRepository, studentMapper, userRepository, passwordEncoder);

        student = Student.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Benali")
                .email("ahmed@example.com")
                .build();
    }

    @Test
    void shouldCreateStudent() {
        StudentRequest request = new StudentRequest();
        request.setFirstName("Ahmed");
        request.setLastName("Benali");
        request.setEmail("ahmed@example.com");
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(departmentRepository.findById(any())).thenReturn(Optional.of(Department.builder().id(1L).name("CS").build()));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(userRepository.save(any())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(studentMapper.toResponse(any(Student.class))).thenReturn(StudentResponse.builder().id(1L).firstName("Ahmed").lastName("Benali").email("ahmed@example.com").build());

        StudentResponse response = studentService.create(request);

        assertEquals("Ahmed", response.getFirstName());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void shouldReturnAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));
        when(studentMapper.toResponse(any(Student.class))).thenReturn(StudentResponse.builder().id(1L).firstName("Ahmed").lastName("Benali").email("ahmed@example.com").build());

        List<StudentResponse> response = studentService.getAll();

        assertEquals(1, response.size());
    }

    @Test
    void shouldFindStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toResponse(any(Student.class))).thenReturn(StudentResponse.builder().id(1L).firstName("Ahmed").lastName("Benali").email("ahmed@example.com").build());

        StudentResponse response = studentService.getById(1L);

        assertEquals("ahmed@example.com", response.getEmail());
    }

    @Test
    void shouldThrowWhenStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getById(99L));
    }
}
