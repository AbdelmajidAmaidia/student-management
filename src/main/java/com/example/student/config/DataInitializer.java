package com.example.student.config;

import com.example.student.entity.Course;
import com.example.student.entity.Department;
import com.example.student.entity.Enrollment;
import com.example.student.entity.Gender;
import com.example.student.entity.Grade;
import com.example.student.entity.Role;
import com.example.student.entity.RoleName;
import com.example.student.entity.Semester;
import com.example.student.entity.Student;
import com.example.student.entity.StudentStatus;
import com.example.student.entity.Teacher;
import com.example.student.entity.User;
import com.example.student.repository.CourseRepository;
import com.example.student.repository.DepartmentRepository;
import com.example.student.repository.EnrollmentRepository;
import com.example.student.repository.GradeRepository;
import com.example.student.repository.RoleRepository;
import com.example.student.repository.StudentRepository;
import com.example.student.repository.TeacherRepository;
import com.example.student.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Random RANDOM = new Random();

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (departmentRepository.count() > 0) {
            return;
        }

        Role adminRole = createRole(RoleName.ADMIN);
        Role teacherRole = createRole(RoleName.TEACHER);
        Role studentRole = createRole(RoleName.STUDENT);

        createAdminUser(adminRole);

        List<Department> departments = seedDepartments();
        List<Teacher> teachers = seedTeachers(departments, teacherRole);
        List<Course> courses = seedCourses(teachers);
        List<Student> students = seedStudents(departments, studentRole);
        seedEnrollments(students, courses);
    }

    private Role createRole(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(Role.builder().name(roleName).build()));
    }

    private void createAdminUser(Role adminRole) {
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .email("admin@student-management.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .roles(Set.of(adminRole))
                    .build());
        }
    }

    private List<Department> seedDepartments() {
        List<Department> departments = List.of(
                Department.builder().name("Computer Science").description("Software and systems engineering").build(),
                Department.builder().name("Mathematics").description("Applied and pure mathematics").build(),
                Department.builder().name("Physics").description("Modern and theoretical physics").build(),
                Department.builder().name("Business").description("Management and entrepreneurship").build(),
                Department.builder().name("Biology").description("Life sciences and biotechnology").build());
        return departmentRepository.saveAll(departments);
    }

    private List<Teacher> seedTeachers(List<Department> departments, Role teacherRole) {
        List<Teacher> teachers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            User user = userRepository.save(User.builder()
                    .username("teacher" + i)
                    .email("teacher" + i + "@student-management.com")
                    .password(passwordEncoder.encode("Teacher@123"))
                    .roles(Set.of(teacherRole))
                    .build());

            Teacher teacher = Teacher.builder()
                    .firstName("TeacherFirst" + i)
                    .lastName("TeacherLast" + i)
                    .email("teacher" + i + "@school.com")
                    .phone("+212600000" + String.format("%03d", i))
                    .department(departments.get(i % departments.size()))
                    .user(user)
                    .build();
            teachers.add(teacher);
        }
        return teacherRepository.saveAll(teachers);
    }

    private List<Course> seedCourses(List<Teacher> teachers) {
        List<Course> courses = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            courses.add(Course.builder()
                    .title("Course " + i)
                    .description("Comprehensive course description for course " + i)
                    .credits((i % 5) + 1)
                    .teacher(teachers.get(i % teachers.size()))
                    .build());
        }
        return courseRepository.saveAll(courses);
    }

    private List<Student> seedStudents(List<Department> departments, Role studentRole) {
        List<Student> students = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            User user = userRepository.save(User.builder()
                    .username("student" + i)
                    .email("student" + i + "@student-management.com")
                    .password(passwordEncoder.encode("Student@123"))
                    .roles(Set.of(studentRole))
                    .build());

            students.add(Student.builder()
                    .firstName("StudentFirst" + i)
                    .lastName("StudentLast" + i)
                    .email("student" + i + "@school.com")
                    .phone("+212700000" + String.format("%03d", i))
                    .birthDate(LocalDate.now().minusYears(18 + (i % 6)).minusDays(i))
                    .gender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE)
                    .address("Address " + i)
                    .department(departments.get(i % departments.size()))
                    .enrollmentDate(LocalDate.now().minusYears(1).minusDays(i))
                    .status(i % 7 == 0 ? StudentStatus.INACTIVE : StudentStatus.ACTIVE)
                    .user(user)
                    .build());
        }
        return studentRepository.saveAll(students);
    }

    private void seedEnrollments(List<Student> students, List<Course> courses) {
        Set<String> uniquePairs = new HashSet<>();
        int created = 0;

        while (created < 100) {
            Student student = students.get(RANDOM.nextInt(students.size()));
            Course course = courses.get(RANDOM.nextInt(courses.size()));
            String key = student.getId() + "-" + course.getId();
            if (!uniquePairs.add(key)) {
                continue;
            }

            Enrollment enrollment = enrollmentRepository.save(Enrollment.builder()
                    .student(student)
                    .course(course)
                    .enrollmentDate(LocalDate.now().minusMonths(RANDOM.nextInt(12) + 1L))
                    .build());

            Grade grade = Grade.builder()
                    .score(8 + (RANDOM.nextDouble() * 12))
                    .semester(Semester.values()[RANDOM.nextInt(Semester.values().length)])
                    .enrollment(enrollment)
                    .build();
            gradeRepository.save(grade);
            created++;
        }
    }
}
