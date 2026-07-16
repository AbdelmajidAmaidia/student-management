package com.example.student.repository;

import com.example.student.entity.Teacher;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByEmail(String email);
    Optional<Teacher> findByUserUsername(String username);
}
