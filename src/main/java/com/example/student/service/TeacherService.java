package com.example.student.service;

import com.example.student.dto.teacher.TeacherRequest;
import com.example.student.dto.teacher.TeacherResponse;
import java.util.List;

public interface TeacherService {
    TeacherResponse create(TeacherRequest request);
    TeacherResponse getById(Long id);
    List<TeacherResponse> getAll();
    TeacherResponse update(Long id, TeacherRequest request);
    void delete(Long id);
}
