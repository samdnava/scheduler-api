package com.sam.scheduler_api.controller;

import com.sam.scheduler_api.model.Course;
import com.sam.scheduler_api.repository.CourseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")

public class CoursesController {
    private final CourseRepository courseRepository;

    public CoursesController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Course> getAllCourse() {
        return courseRepository.findAll();
    }

    @PostMapping
    public Course createCourse(@RequestBody Course newCourse) {
        return courseRepository.save(newCourse);
    }

}
