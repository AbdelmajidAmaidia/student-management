package com.example.student.service.impl;

import com.example.student.dto.teacher.TeacherRequest;
import com.example.student.dto.teacher.TeacherResponse;
import com.example.student.entity.AppUser;
import com.example.student.entity.Role;
import com.example.student.entity.Teacher;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.TeacherMapper;
import com.example.student.repository.AppUserRepository;
import com.example.student.repository.TeacherRepository;
import com.example.student.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TeacherResponse create(TeacherRequest request) {
        if (teacherRepository.existsByEmail(request.getEmail()) || userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already used");
        }

        Teacher teacher = Teacher.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .specialty(request.getSpecialty())
                .build();

        Teacher saved = teacherRepository.save(teacher);
        createUser(saved.getEmail(), Role.TEACHER);

        return teacherMapper.toResponse(saved);
    }

    @Override
    public TeacherResponse update(Long id, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));

        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setSpecialty(request.getSpecialty());

        if (!teacher.getEmail().equals(request.getEmail()) && teacherRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already used");
        }

        syncUserEmail(teacher.getEmail(), request.getEmail());
        teacher.setEmail(request.getEmail());

        return teacherMapper.toResponse(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherResponse getById(Long id) {
        return teacherMapper.toResponse(teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponse> getAll() {
        return teacherRepository.findAll().stream().map(teacherMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
        userRepository.findByEmail(teacher.getEmail()).ifPresent(userRepository::delete);
        teacherRepository.delete(teacher);
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
