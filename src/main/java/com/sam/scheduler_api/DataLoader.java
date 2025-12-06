package com.sam.scheduler_api;

import com.sam.scheduler_api.model.Student;
import com.sam.scheduler_api.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;

    // Dependency Injection: Spring gives us the repository automatically
    public DataLoader(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- LOADING DATA ---");

        // 1. Create a student
        Student sam = new Student("101", "Sam", "N", "sam@test.com");

        // 2. Save it using the Magic Repository
        studentRepository.save(sam);

        System.out.println("Saved Student: " + sam.getFirstName());
    }
}