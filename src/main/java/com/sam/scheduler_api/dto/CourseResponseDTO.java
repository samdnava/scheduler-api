package com.sam.scheduler_api.dto;

import com.sam.scheduler_api.model.Course;

public record CourseResponseDTO(String id, String courseName, double credits) {
    public static CourseResponseDTO fromEntity(Course course) {
        return new CourseResponseDTO(
                course.getCourseId(),
                course.getName(),
                course.getCredits()
        );
    }

}
