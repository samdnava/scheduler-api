package com.sam.scheduler_api.service;

import com.sam.scheduler_api.dto.StudentResponseDTO;
import com.sam.scheduler_api.dto.ScheduleItemDTO;
import com.sam.scheduler_api.exception.AlreadyEnrolledException;
import com.sam.scheduler_api.exception.CourseFullException;
import com.sam.scheduler_api.exception.ResourceNotFoundException;
import com.sam.scheduler_api.exception.TimeConflictException;
import com.sam.scheduler_api.model.Section;
import com.sam.scheduler_api.model.Student;
import com.sam.scheduler_api.repository.SectionRepository;
import com.sam.scheduler_api.repository.StudentRepository;
import jakarta.transaction.Transactional;
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

    // Logic 3: The Complex Enrollment Logic
    @Transactional
    public StudentResponseDTO enrollStudent(String studentId, String crn) {
        // 1. Find the Student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        // 2. Find the Section
        Section section = sectionRepository.findById(crn)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with CRN: " + crn));
        // 3.1. Check if already enrolled
        if (student.getSections().contains(section)) {
            throw new AlreadyEnrolledException("Student is already in section: " + crn);
        }
        // 3.2. Check Capacity
        // We use >= because we are about to add the +1st student
        if (section.getStudents().size() >= section.getCapacity()) {
            throw new CourseFullException("Section " + crn + " is full!");
        }

        // 3.3. Check for Time Conflicts
        for (Section existingSection : student.getSections()) {
            if (existingSection.getDayOfWeek().equals(section.getDayOfWeek())
                    && existingSection.getTimeOfDay().equals(section.getTimeOfDay())) {

                throw new TimeConflictException("Time conflict! You already have a class on " + section.getDayOfWeek() + " at " + section.getTimeOfDay());
            }
        }

        // 4. Link them
        student.getSections().add(section);
        section.getStudents().add(student);

        // 5. Save and Return
        Student savedStudent = studentRepository.save(student);
        return StudentResponseDTO.fromEntity(savedStudent);
    }

    public void removeStudentFromSection(String studentId, String crn) {
        // 1. Find the Student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        // 2. Find the Section
        Section section = sectionRepository.findById(crn)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with CRN: " + crn));

        // 3. Check if the relationship exists
        if (!student.getSections().contains(section)) {
            throw new ResourceNotFoundException("Student is not enrolled in section: " + crn);
        }

        // 4. Remove the relationship and Save
        student.getSections().remove(section);
        studentRepository.save(student);
    }

    public List<ScheduleItemDTO> getStudentSchedule(String studentId) {
        // 1. Fetch the student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

        // 2. Transform the data
        // We take the Set<Section>, stream it, and map each one to a simplified DTO
        return student.getSections().stream()
                .map(ScheduleItemDTO::fromEntity)
                .toList();
    }
}
