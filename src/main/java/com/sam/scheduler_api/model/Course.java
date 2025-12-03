package com.sam.scheduler_api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    private String courseId; // e.g., "CSI 403"
    private String name; // e.g., "Algorithms"
    private double credits;

    // NEW: The list of courses you must take Before this one
    private List<Course> preRequisites;

    public Course(String courseId, String name, double credits) {
        this.courseId = courseId;
        this.name = name;
        this.credits = credits;
        this.preRequisites = new ArrayList<>(); // NEW: Initialize the list
    }

    public String getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    //NEW: Method to add a requirement
    public void addPreRequisite(Course preRequisite) {
        preRequisites.add(preRequisite);
    }

    //NEW: Method to get the list (so we can check it later)
    public List<Course> getPreRequisites() {
        return preRequisites;
    }

    public double getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return courseId + ": " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

}
