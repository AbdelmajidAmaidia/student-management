package com.example.student.service.impl;

import com.example.student.dto.department.DepartmentRequest;
import com.example.student.dto.department.DepartmentResponse;
import com.example.student.entity.Department;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.DepartmentMapper;
import com.example.student.repository.DepartmentRepository;
import com.example.student.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Department already exists");
        }

        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        return departmentMapper.toResponse(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getById(Long id) {
        return departmentMapper.toResponse(departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll().stream().map(departmentMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));
        departmentRepository.delete(department);
    }
}
