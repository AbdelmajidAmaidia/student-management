package com.example.student.validation;

import com.example.student.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class RequestValidator {

    public void requirePositiveId(Long id, String field) {
        if (id == null || id <= 0) {
            throw new ValidationException(field + " must be a positive number");
        }
    }
}
