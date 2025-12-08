package com.sam.scheduler_api.service;

import com.sam.scheduler_api.dto.CourseResponseDTO;
import com.sam.scheduler_api.model.Course;
import com.sam.scheduler_api.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseResponseDTO> findAllCourses() {
        return courseRepository.findAll().stream().map(CourseResponseDTO::fromEntity).toList();
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }
}
