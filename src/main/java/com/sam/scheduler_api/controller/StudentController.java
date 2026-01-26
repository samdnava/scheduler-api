package com.sam.scheduler_api.controller;

import com.sam.scheduler_api.model.Student;
import com.sam.scheduler_api.dto.StudentResponseDTO;
import com.sam.scheduler_api.dto.ScheduleItemDTO;
import com.sam.scheduler_api.service.StudentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    // Dependency Injection: Spring automatically hands us the Repository
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Endpoint: GET /students
    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        // This automatically converts the Java List to JSON
        return studentService.findAllStudents();
    }

    // Endpoint: GET /student/{id}/schedule
    @GetMapping("/{studentId}/schedule")
    public List<ScheduleItemDTO> getStudentSchedule(@PathVariable String studentId) {
        return studentService.getStudentSchedule(studentId);
    }

    // Endpoint: POST /students
    // Action: Save a new student
    @PostMapping
    // @Valid: Tells Spring "Check the newStudent against the rules in the Student class.
    // If it fails, reject the request immediately."
    public StudentResponseDTO registerStudent(@Valid @RequestBody Student newStudent) {
        return studentService.registerStudent(newStudent);
    }

    // Endpoint: POST /students/999/enroll/CRN-101
    @PostMapping("/{studentId}/enroll/{crn}")
    public StudentResponseDTO enrollStudent(@PathVariable String studentId, @PathVariable String crn) {
        return studentService.enrollStudent(studentId, crn);
    }

    // Endpoint: DELETE /students/{studentId}/enroll/{crn}
    @DeleteMapping("/{studentId}/enroll/{crn}")
    public ResponseEntity<Void> unenrollStudent(@PathVariable String studentId, @PathVariable String crn) {
        studentService.removeStudentFromSection(studentId, crn);
        return ResponseEntity.noContent().build();
    }
}
