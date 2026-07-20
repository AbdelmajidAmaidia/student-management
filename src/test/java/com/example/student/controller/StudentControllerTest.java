package com.example.student.controller;

<<<<<<< HEAD
import com.example.student.dto.student.StudentResponse;
import com.example.student.service.StudentService;
=======
import com.example.student.dto.StudentRequest;
import com.example.student.dto.StudentResponse;
import com.example.student.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
>>>>>>> 3901877 (from vs code)
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
<<<<<<< HEAD
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(com.example.student.config.SecurityConfig.class)
=======
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
>>>>>>> 3901877 (from vs code)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

<<<<<<< HEAD
    @MockBean
    private StudentService studentService;

    @MockBean
    private com.example.student.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private com.example.student.security.CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetStudentsAsAdmin() throws Exception {
        when(studentService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void shouldGetStudentByIdAsTeacher() throws Exception {
        when(studentService.getById(1L)).thenReturn(StudentResponse.builder().id(1L).firstName("A").lastName("B").email("a@b.com").build());

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STUDENT", username = "student1@student-management.com")
    void shouldGetCurrentStudentAsStudent() throws Exception {
        when(studentService.getCurrentStudent("student1@student-management.com")).thenReturn(StudentResponse.builder().id(1L).firstName("A").lastName("B").email("a@b.com").build());

        mockMvc.perform(get("/api/students/me"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void shouldForbidStudentListForStudentRole() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isForbidden());
=======
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void shouldCreateStudent() throws Exception {
        StudentRequest request = new StudentRequest("Jane", "Doe", "jane@example.com", 21);
        StudentResponse response = new StudentResponse(1L, "Jane", "Doe", "jane@example.com", 21);

        when(studentService.createStudent(any(StudentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    void shouldRejectInvalidPayload() throws Exception {
        StudentRequest request = new StudentRequest("", "Doe", "invalid-email", 10);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
>>>>>>> 3901877 (from vs code)
    }
}
