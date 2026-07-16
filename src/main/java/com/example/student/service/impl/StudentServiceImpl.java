package com.example.student.service.impl;

import com.example.student.dto.student.StudentRequest;
import com.example.student.dto.student.StudentResponse;
import com.example.student.entity.Department;
import com.example.student.entity.Student;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.StudentMapper;
import com.example.student.repository.DepartmentRepository;
import com.example.student.repository.StudentRepository;
import com.example.student.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentResponse create(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Student email already exists");
        }
        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .address(request.getAddress())
                .department(findDepartment(request.getDepartmentId()))
                .enrollmentDate(request.getEnrollmentDate())
                .status(request.getStatus())
                .build();
        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse getById(Long id) {
        return studentMapper.toResponse(findStudent(id));
    }

    @Override
    public StudentResponse getCurrentStudent(String username) {
        return studentMapper.toResponse(studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user: " + username)));
    }

    @Override
    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream().map(studentMapper::toResponse).toList();
    }

    @Override
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findStudent(id);
        if (!student.getEmail().equalsIgnoreCase(request.getEmail()) && studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Student email already exists");
        }
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setBirthDate(request.getBirthDate());
        student.setGender(request.getGender());
        student.setAddress(request.getAddress());
        student.setDepartment(findDepartment(request.getDepartmentId()));
        student.setEnrollmentDate(request.getEnrollmentDate());
        student.setStatus(request.getStatus());
        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public void delete(Long id) {
        studentRepository.delete(findStudent(id));
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }
}
