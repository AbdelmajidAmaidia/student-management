package com.example.student.controller;

import com.example.student.dto.student.StudentResponse;
import com.example.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void shouldGetAllStudents() {
        when(studentService.getAll()).thenReturn(List.of());

        ResponseEntity<List<StudentResponse>> response = studentController.getAll();
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void shouldGetStudentById() {
        when(studentService.getById(1L)).thenReturn(StudentResponse.builder().id(1L).firstName("A").lastName("B").email("a@b.com").build());

        ResponseEntity<StudentResponse> response = studentController.getById(1L);
        assertEquals(200, response.getStatusCode().value());
    }
}
