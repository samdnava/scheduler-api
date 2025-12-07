package com.sam.scheduler_api.controller;

import com.sam.scheduler_api.model.Student;
import com.sam.scheduler_api.repository.StudentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    // Dependency Injection: Spring automatically hands us the Repository
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // The Endpoint: GET / students
    @GetMapping
    public List<Student> getAllStudents() {
        // This automatically converts the Java List to JSON
        return studentRepository.findAll();
    }
}
