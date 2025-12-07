package com.sam.scheduler_api.dto;

import com.sam.scheduler_api.model.Student;

import java.util.List;
import java.util.stream.Collectors;

// A Record is a "Data Carrier". It holds data but has no logic.
// We are intentionally leaving out "password" or intentional stuff here.
public record StudentResponseDTO(String id,
                                 String fullName, // Example: We will combine First + Last Name
                                 String email,
                                 List<String> enrolledClasses // Example: Just the names, not the full objects
) {
    // Static Mapper: Converts Entity -> DTO
    public static StudentResponseDTO fromEntity(Student student) {
        // 1. Combine names
        String fullName = student.getFirstName() + " " + student.getLastName();

        // 2. Transform the schedule list into just Course Names (Strings)
        // This is a "Stream" - it loops through the sections and grabs the course name
        List<String> classNames = student.getSchedule().stream()
                .map(section -> section.getCourse().getName())
                .collect(Collectors.toList());

        // 3. Return the clean DTO
        return new StudentResponseDTO(
                student.getId(),
                fullName,
                student.getEmail(),
                classNames
        );
    }
}
