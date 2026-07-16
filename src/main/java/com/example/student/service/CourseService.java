package com.example.student.service;

import com.example.student.dto.course.CourseRequest;
import com.example.student.dto.course.CourseResponse;

import java.util.List;

public interface CourseService {
    CourseResponse create(CourseRequest request);
    CourseResponse update(Long id, CourseRequest request);
    CourseResponse getById(Long id);
    List<CourseResponse> getAll();
    void delete(Long id);
}
