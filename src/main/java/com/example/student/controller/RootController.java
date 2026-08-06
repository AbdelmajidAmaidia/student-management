package com.example.student.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String ping() {
        return "zaynoubty ya 3assla ";
    }
}
