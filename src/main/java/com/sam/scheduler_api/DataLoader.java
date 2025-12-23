package com.sam.scheduler_api;

import com.sam.scheduler_api.model.Student;
import com.sam.scheduler_api.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final StudentService studentService;

    // Dependency Injection: Spring gives us the repository automatically
    public DataLoader(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- LOADING DATA ---");

        // 1. Create a student
        Student student = new Student(null, "Student", "Test", "test@test.com");

        // 2. Save it using the Service (which handles DTO conversion logic)
        studentService.registerStudent(student);

        System.out.println("Saved Student: " + student.getFirstName());
    }
}
