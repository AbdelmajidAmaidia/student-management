package com.example.student.controller;

import com.example.student.dto.student.StudentResponse;
import com.example.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(com.example.student.config.SecurityConfig.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    }
}
