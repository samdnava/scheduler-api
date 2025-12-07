package com.sam.scheduler_api.service;

import com.sam.scheduler_api.dto.StudentResponseDTO;
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
    public List<StudentResponseDTO> findAllStudents() {
        return studentRepository.findAll()
                .stream().map(StudentResponseDTO::fromEntity).toList();
    }

    // Logic 2: Register a new student
    public StudentResponseDTO registerStudent(Student newStudent) {
        Student savedStudent = studentRepository.save(newStudent);
        return StudentResponseDTO.fromEntity(savedStudent);
    }

    // Logic 3: The Complex Enrollment Logic (Moved from Controller)
    public StudentResponseDTO enrollStudent(String studentId, String crn) {
        // 1. Find the Student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        // 2. Find the Section
        Section section = sectionRepository.findById(crn)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with CRN: " + crn));
        // 3. Link them
        student.getSchedule().add(section);
        Student savedStudent = studentRepository.save(student);
        // 4. Save and Return
        return StudentResponseDTO.fromEntity(savedStudent);
    }
}
