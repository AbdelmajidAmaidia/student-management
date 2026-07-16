package com.example.student.service.impl;

import com.example.student.dto.department.DepartmentRequest;
import com.example.student.dto.department.DepartmentResponse;
import com.example.student.entity.Department;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.DepartmentMapper;
import com.example.student.repository.DepartmentRepository;
import com.example.student.service.DepartmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Department name already exists");
        }
        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    public DepartmentResponse getById(Long id) {
        return departmentMapper.toResponse(findDepartment(id));
    }

    @Override
    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll().stream().map(departmentMapper::toResponse).toList();
    }

    @Override
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = findDepartment(id);
        if (!department.getName().equalsIgnoreCase(request.getName())
                && departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Department name already exists");
        }
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    public void delete(Long id) {
        departmentRepository.delete(findDepartment(id));
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }
}
