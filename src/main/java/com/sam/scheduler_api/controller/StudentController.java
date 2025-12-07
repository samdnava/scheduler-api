package com.sam.scheduler_api.controller;

import com.sam.scheduler_api.model.Section;
import com.sam.scheduler_api.model.Student;
import com.sam.scheduler_api.repository.SectionRepository;
import com.sam.scheduler_api.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;

    // Dependency Injection: Spring automatically hands us the Repository
    public StudentController(StudentRepository studentRepository, SectionRepository sectionRepository) {
        this.studentRepository = studentRepository;
        this.sectionRepository = sectionRepository;
    }

    // The Endpoint: GET / students
    @GetMapping
    public List<Student> getAllStudents() {
        // This automatically converts the Java List to JSON
        return studentRepository.findAll();
    }

    // Endpoint: POST /students
    // Action: Save a new student
    @PostMapping
    public Student registerStudent(@RequestBody Student newStudent) {
        return studentRepository.save(newStudent);
    }

    // Endpoint: POST /students/999/enroll/CRN-101
    @PostMapping("/{studentId}/enroll/{crn}")
    public Student enrollStudent(
            @PathVariable String studentId,
            @PathVariable String crn
    ) {
        // 1. Find the Student (throw error if not found)
        Student student = studentRepository.findById(studentId).orElseThrow();

        // 2. Find the Section (throw error if not found)
        Section section = sectionRepository.findById(crn).orElseThrow();

        // 3. Add Section to Student's schedule
        student.getSchedule().add(section);

        // 4. Save the Student (Hibernate updates the 'enrollments' table automatically)
        return studentRepository.save(student);
    }

}
