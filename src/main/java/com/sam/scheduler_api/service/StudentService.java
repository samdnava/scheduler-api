package com.sam.scheduler_api.service;

import com.sam.scheduler_api.exception.ResourceNotFoundException;
import com.sam.scheduler_api.model.Section;
import com.sam.scheduler_api.model.Student;
import com.sam.scheduler_api.repository.SectionRepository;
import com.sam.scheduler_api.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;

    // Constructor Injection
    public StudentService(StudentRepository studentRepository, SectionRepository sectionRepository) {
        this.studentRepository = studentRepository;
        this.sectionRepository = sectionRepository;
    }

    // Logic 1: Get all students
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    // Logic 2: Register a new student
    public Student registerStudent(Student newStudent) {
        return studentRepository.save(newStudent);
    }

    // Logic 3: The Complex Enrollment Logic (Moved from Controller)
    public Student enrollStudent(String studentId, String crn) {
        // 1. Find the Student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        // 2. Find the Section
        Section section = sectionRepository.findById(crn)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with CRN: " + crn));
        // 3. Link them
        student.getSchedule().add(section);
        // 4. Save
        return studentRepository.save(student);
    }
}
