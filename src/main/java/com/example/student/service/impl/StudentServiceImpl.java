package com.example.student.service.impl;

import com.example.student.dto.student.StudentRequest;
import com.example.student.dto.student.StudentResponse;
import com.example.student.entity.AppUser;
import com.example.student.entity.Department;
import com.example.student.entity.Role;
import com.example.student.entity.Student;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.StudentMapper;
import com.example.student.repository.AppUserRepository;
import com.example.student.repository.DepartmentRepository;
import com.example.student.repository.StudentRepository;
import com.example.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentMapper studentMapper;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public StudentResponse create(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail()) || userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already used");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + request.getDepartmentId()));

        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .address(request.getAddress())
                .department(department)
                .enrollmentDate(request.getEnrollmentDate())
                .status(request.getStatus())
                .build();

        Student saved = studentRepository.save(student);
        createUser(saved.getEmail(), Role.STUDENT);

        return studentMapper.toResponse(saved);
    }

    @Override
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + request.getDepartmentId()));

        if (!student.getEmail().equals(request.getEmail()) && studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already used");
        }

        syncUserEmail(student.getEmail(), request.getEmail());

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setBirthDate(request.getBirthDate());
        student.setGender(request.getGender());
        student.setAddress(request.getAddress());
        student.setDepartment(department);
        student.setEnrollmentDate(request.getEnrollmentDate());
        student.setStatus(request.getStatus());

        return studentMapper.toResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return studentMapper.toResponse(studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getCurrentStudent(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for user: " + email));
        return studentMapper.toResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream().map(studentMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
        userRepository.findByEmail(student.getEmail()).ifPresent(userRepository::delete);
        studentRepository.delete(student);
    }

    private void createUser(String email, Role role) {
        userRepository.save(AppUser.builder()
                .email(email)
                .password(passwordEncoder.encode("password123"))
                .role(role)
                .build());
    }

    private void syncUserEmail(String oldEmail, String newEmail) {
        userRepository.findByEmail(oldEmail).ifPresent(user -> user.setEmail(newEmail));
    }
}
