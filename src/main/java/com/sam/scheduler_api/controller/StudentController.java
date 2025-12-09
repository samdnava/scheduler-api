package com.sam.scheduler_api.controller;

import com.sam.scheduler_api.dto.StudentResponseDTO;
import com.sam.scheduler_api.model.Student;
import com.sam.scheduler_api.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    // Dependency Injection: Spring automatically hands us the Repository
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // The Endpoint: GET / students
    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        // This automatically converts the Java List to JSON
        return studentService.findAllStudents();
    }

    // Endpoint: POST /students
    // Action: Save a new student
    @PostMapping
    public StudentResponseDTO registerStudent(@RequestBody Student newStudent) {
        return studentService.registerStudent(newStudent);
    }

    // Endpoint: POST /students/999/enroll/CRN-101
    @PostMapping("/{studentId}/enroll/{crn}")
    public StudentResponseDTO enrollStudent(@PathVariable String studentId, @PathVariable String crn) {
        return studentService.enrollStudent(studentId, crn);
    }

}
