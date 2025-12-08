package com.sam.scheduler_api.controller;

import com.sam.scheduler_api.dto.CourseResponseDTO;
import com.sam.scheduler_api.model.Course;
import com.sam.scheduler_api.repository.CourseRepository;
import com.sam.scheduler_api.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")

public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseResponseDTO> getAllCourse() {
        return courseService.findAllCourses();
    }

    @PostMapping
    public CourseResponseDTO createCourse(@RequestBody Course newCourse) {
        return courseService.saveCourse(newCourse);
    }

}
