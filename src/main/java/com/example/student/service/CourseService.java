package com.example.student.service;

import com.example.student.dto.course.CourseRequest;
import com.example.student.dto.course.CourseResponse;
import java.util.List;

public interface CourseService {
    CourseResponse create(CourseRequest request);
    CourseResponse getById(Long id);
    List<CourseResponse> getAll();
    CourseResponse update(Long id, CourseRequest request);
    void delete(Long id);
}
