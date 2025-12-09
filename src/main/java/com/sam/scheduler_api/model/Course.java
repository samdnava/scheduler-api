package com.sam.scheduler_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    private String courseId; // Primary Key

    private String name;
    private Double credits;

    // JPA Constructor
    public Course() {
    }

    // Standard Constructor
    public Course(String courseId, String name, Double credits) {
        this.courseId = courseId;
        this.name = name;
        this.credits = credits;
    }

    // Getters and Setters

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }
}
