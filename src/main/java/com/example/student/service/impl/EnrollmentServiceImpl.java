package com.example.student.service.impl;

import com.example.student.dto.enrollment.EnrollmentRequest;
import com.example.student.dto.enrollment.EnrollmentResponse;
import com.example.student.entity.Course;
import com.example.student.entity.Enrollment;
import com.example.student.entity.Grade;
import com.example.student.entity.Student;
import com.example.student.exception.DuplicateResourceException;
import com.example.student.exception.ResourceNotFoundException;
import com.example.student.mapper.EnrollmentMapper;
import com.example.student.repository.CourseRepository;
import com.example.student.repository.EnrollmentRepository;
import com.example.student.repository.GradeRepository;
import com.example.student.repository.StudentRepository;
import com.example.student.service.EnrollmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional
    public EnrollmentResponse create(EnrollmentRequest request) {
        if (enrollmentRepository.findByStudentIdAndCourseId(request.getStudentId(), request.getCourseId()).isPresent()) {
            throw new DuplicateResourceException("Student is already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(findStudent(request.getStudentId()))
                .course(findCourse(request.getCourseId()))
                .enrollmentDate(request.getEnrollmentDate())
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        Grade grade = Grade.builder()
                .score(request.getGrade())
                .semester(request.getSemester())
                .enrollment(savedEnrollment)
                .build();
        gradeRepository.save(grade);
        savedEnrollment.setGrade(grade);

        return enrollmentMapper.toResponse(savedEnrollment);
    }

    @Override
    public EnrollmentResponse getById(Long id) {
        return enrollmentMapper.toResponse(findEnrollment(id));
    }

    @Override
    public List<EnrollmentResponse> getAll() {
        return enrollmentRepository.findAll().stream().map(enrollmentMapper::toResponse).toList();
    }

    @Override
    public List<EnrollmentResponse> getByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream().map(enrollmentMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public EnrollmentResponse update(Long id, EnrollmentRequest request) {
        Enrollment enrollment = findEnrollment(id);
        Student student = findStudent(request.getStudentId());
        Course course = findCourse(request.getCourseId());

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(request.getEnrollmentDate());

        Grade grade = enrollment.getGrade();
        if (grade == null) {
            grade = Grade.builder().enrollment(enrollment).build();
        }
        grade.setScore(request.getGrade());
        grade.setSemester(request.getSemester());
        gradeRepository.save(grade);
        enrollment.setGrade(grade);

        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    public void delete(Long id) {
        enrollmentRepository.delete(findEnrollment(id));
    }

    private Enrollment findEnrollment(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }
}
