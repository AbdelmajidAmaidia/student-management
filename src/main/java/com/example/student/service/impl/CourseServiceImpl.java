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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponse create(CourseRequest request) {
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .credits(request.getCredits())
                .teacher(findTeacher(request.getTeacherId()))
                .build();
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse getById(Long id) {
        return courseMapper.toResponse(findCourse(id));
    }

    @Override
    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream().map(courseMapper::toResponse).toList();
    }

    @Override
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = findCourse(id);
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setTeacher(findTeacher(request.getTeacherId()));
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public void delete(Long id) {
        courseRepository.delete(findCourse(id));
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private Teacher findTeacher(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
    }
}
