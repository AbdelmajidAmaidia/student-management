package com.example.student.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Student with email already exists: " + email);
    }
}
