package com.example.student.config;

import com.example.student.entity.*;
import com.example.student.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeRepository gradeRepository;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            if (departmentRepository.count() > 0) {
                return;
            }

            userRepository.save(AppUser.builder()
                    .email("admin@student-management.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build());

            List<Department> departments = createDepartments();
            List<Teacher> teachers = createTeachers();
            List<Student> students = createStudents(departments);
            List<Course> courses = createCourses(teachers);
            createEnrollments(students, courses);
        };
    }

    private List<Department> createDepartments() {
        List<Department> departments = List.of(
                Department.builder().name("Computer Science").description("Software engineering and data").build(),
                Department.builder().name("Mathematics").description("Applied and pure math").build(),
                Department.builder().name("Physics").description("Fundamental and experimental physics").build(),
                Department.builder().name("Economics").description("Micro and macro economics").build(),
                Department.builder().name("Literature").description("Modern and classic literature").build()
        );
        return departmentRepository.saveAll(departments);
    }

    private List<Teacher> createTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Teacher teacher = Teacher.builder()
                    .firstName("Teacher" + i)
                    .lastName("Lastname" + i)
                    .email("teacher" + i + "@student-management.com")
                    .specialty("Specialty " + i)
                    .build();
            teachers.add(teacher);
            userRepository.save(AppUser.builder()
                    .email(teacher.getEmail())
                    .password(passwordEncoder.encode("password123"))
                    .role(Role.TEACHER)
                    .build());
        }
        return teacherRepository.saveAll(teachers);
    }

    private List<Student> createStudents(List<Department> departments) {
        List<Student> students = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Department department = departments.get((i - 1) % departments.size());
            Student student = Student.builder()
                    .firstName("Student" + i)
                    .lastName("Lastname" + i)
                    .email("student" + i + "@student-management.com")
                    .phone("+212600000" + String.format("%03d", i))
                    .birthDate(LocalDate.of(2000, 1, 1).plusDays(i * 40L))
                    .gender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE)
                    .address("Address " + i)
                    .department(department)
                    .enrollmentDate(LocalDate.now().minusDays(i * 7L))
                    .status(StudentStatus.ACTIVE)
                    .build();
            students.add(student);
            userRepository.save(AppUser.builder()
                    .email(student.getEmail())
                    .password(passwordEncoder.encode("password123"))
                    .role(Role.STUDENT)
                    .build());
        }
        return studentRepository.saveAll(students);
    }

    private List<Course> createCourses(List<Teacher> teachers) {
        List<Course> courses = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            courses.add(Course.builder()
                    .title("Course " + i)
                    .description("Description for course " + i)
                    .credits((i % 5) + 1)
                    .teacher(teachers.get((i - 1) % teachers.size()))
                    .build());
        }
        return courseRepository.saveAll(courses);
    }

    private void createEnrollments(List<Student> students, List<Course> courses) {
        Random random = new Random(42);
        int created = 0;
        int attempts = 0;

        while (created < 100 && attempts < 1000) {
            attempts++;
            Student student = students.get(random.nextInt(students.size()));
            Course course = courses.get(random.nextInt(courses.size()));

            if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
                continue;
            }

            Enrollment enrollment = enrollmentRepository.save(Enrollment.builder()
                    .student(student)
                    .course(course)
                    .build());

            Grade grade = gradeRepository.save(Grade.builder()
                    .enrollment(enrollment)
                    .value(8.0 + (random.nextDouble() * 12.0))
                    .semester(created % 2 == 0 ? "S1" : "S2")
                    .build());
            enrollment.setGrade(grade);

            created++;
        }
    }
}
