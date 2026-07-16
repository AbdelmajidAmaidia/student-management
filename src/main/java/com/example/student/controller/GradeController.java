package com.example.student.controller;

import com.example.student.dto.grade.GradeRequest;
import com.example.student.dto.grade.GradeResponse;
import com.example.student.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<GradeResponse> createOrUpdate(@Valid @RequestBody GradeRequest request) {
        return ResponseEntity.ok(gradeService.createOrUpdate(request));
    }

    @GetMapping("/enrollment/{enrollmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<GradeResponse> getByEnrollment(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(gradeService.getByEnrollment(enrollmentId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<List<GradeResponse>> getAll() {
        return ResponseEntity.ok(gradeService.getAll());
    }
}
