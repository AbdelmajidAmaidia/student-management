package com.example.student.service;

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
}
