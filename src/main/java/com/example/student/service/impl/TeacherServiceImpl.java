package com.example.student.service.impl;

import com.example.student.dto.teacher.TeacherRequest;
import com.example.student.dto.teacher.TeacherResponse;
import com.example.student.entity.Department;
import com.example.student.entity.Teacher;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.TeacherMapper;
import com.example.student.repository.DepartmentRepository;
import com.example.student.repository.TeacherRepository;
import com.example.student.service.TeacherService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherMapper teacherMapper;

    @Override
    public TeacherResponse create(TeacherRequest request) {
        if (teacherRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Teacher email already exists");
        }
        Department department = findDepartment(request.getDepartmentId());
        Teacher teacher = Teacher.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .department(department)
                .build();
        return teacherMapper.toResponse(teacherRepository.save(teacher));
    }

    @Override
    public TeacherResponse getById(Long id) {
        return teacherMapper.toResponse(findTeacher(id));
    }

    @Override
    public List<TeacherResponse> getAll() {
        return teacherRepository.findAll().stream().map(teacherMapper::toResponse).toList();
    }

    @Override
    public TeacherResponse update(Long id, TeacherRequest request) {
        Teacher teacher = findTeacher(id);
        if (!teacher.getEmail().equalsIgnoreCase(request.getEmail()) && teacherRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Teacher email already exists");
        }
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());
        teacher.setDepartment(findDepartment(request.getDepartmentId()));
        return teacherMapper.toResponse(teacherRepository.save(teacher));
    }

    @Override
    public void delete(Long id) {
        teacherRepository.delete(findTeacher(id));
    }

    private Teacher findTeacher(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }
}
