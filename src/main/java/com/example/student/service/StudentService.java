package com.example.student.service;

<<<<<<< HEAD
import com.example.student.dto.student.StudentRequest;
import com.example.student.dto.student.StudentResponse;

import java.util.List;

public interface StudentService {
    StudentResponse create(StudentRequest request);
    StudentResponse update(Long id, StudentRequest request);
    StudentResponse getById(Long id);
    StudentResponse getCurrentStudent(String email);
    List<StudentResponse> getAll();
    void delete(Long id);
=======
import com.example.student.dto.StudentRequest;
import com.example.student.dto.StudentResponse;
import com.example.student.entity.Student;
import com.example.student.exception.EmailAlreadyExistsException;
import com.example.student.exception.StudentNotFoundException;
import com.example.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        studentRepository.findByEmail(request.email())
                .ifPresent(student -> {
                    throw new EmailAlreadyExistsException(request.email());
                });

        Student student = new Student(request.firstName(), request.lastName(), request.email(), request.age());
        Student saved = studentRepository.save(student);
        return StudentResponse.from(saved);
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentResponse::from)
                .toList();
    }

    public StudentResponse getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(StudentResponse::from)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        studentRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new EmailAlreadyExistsException(request.email());
                });

        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        student.setAge(request.age());

        return StudentResponse.from(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }
>>>>>>> 3901877 (from vs code)
}
