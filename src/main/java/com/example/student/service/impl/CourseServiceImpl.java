package com.example.student.service.impl;

import com.example.student.dto.course.CourseRequest;
import com.example.student.dto.course.CourseResponse;
import com.example.student.entity.Course;
import com.example.student.entity.Teacher;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.CourseMapper;
import com.example.student.repository.CourseRepository;
import com.example.student.repository.TeacherRepository;
import com.example.student.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponse create(CourseRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId()));

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .credits(request.getCredits())
                .teacher(teacher)
                .build();

        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + id));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId()));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setTeacher(teacher);

        return courseMapper.toResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getById(Long id) {
        return courseMapper.toResponse(courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream().map(courseMapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + id));
        courseRepository.delete(course);
    }
}
