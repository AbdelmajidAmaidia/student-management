package com.example.student.service;

import com.example.student.dto.student.StudentRequest;
import com.example.student.dto.student.StudentResponse;
import java.util.List;

public interface StudentService {
    StudentResponse create(StudentRequest request);
    StudentResponse getById(Long id);
    StudentResponse getCurrentStudent(String username);
    List<StudentResponse> getAll();
    StudentResponse update(Long id, StudentRequest request);
    void delete(Long id);
}
