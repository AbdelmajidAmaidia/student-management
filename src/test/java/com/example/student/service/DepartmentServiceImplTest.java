package com.example.student.service;

import com.example.student.dto.department.DepartmentRequest;
import com.example.student.entity.Department;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.DepartmentMapper;
import com.example.student.repository.DepartmentRepository;
import com.example.student.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private DepartmentMapper departmentMapper;
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private DepartmentRequest request;

    @BeforeEach
    void setUp() {
        request = new DepartmentRequest();
        request.setName("Computer Science");
        request.setDescription("desc");
    }

    @Test
    void shouldCreateDepartment() {
        when(departmentRepository.existsByNameIgnoreCase("Computer Science")).thenReturn(false);
        when(departmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(departmentMapper.toResponse(any())).thenReturn(null);

        assertDoesNotThrow(() -> departmentService.create(request));
    }

    @Test
    void shouldThrowWhenDuplicateDepartment() {
        when(departmentRepository.existsByNameIgnoreCase("Computer Science")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> departmentService.create(request));
    }

    @Test
    void shouldUpdateDepartment() {
        Department department = Department.builder().id(1L).name("Old").build();
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentMapper.toResponse(any())).thenReturn(null);

        assertDoesNotThrow(() -> departmentService.update(1L, request));
        assertEquals("Computer Science", department.getName());
    }

    @Test
    void shouldThrowWhenDepartmentNotFoundOnUpdate() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departmentService.update(1L, request));
    }

    @Test
    void shouldGetAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(List.of());

        assertNotNull(departmentService.getAll());
    }
}
