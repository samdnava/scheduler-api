package com.sam.scheduler_api.model;

public class Section {
    private String crn; // Unique ID (e.g., 12345)
    private Course course; // The Course object
    private Professor instructor; // The Professor object
    private String day; // e.g., "Monday"
    private String time; // e.g., "10:00 AM"

    public Section(String crn, Course course, Professor instructor, String day, String time) {
        this.crn = crn;
        this.course = course;
        this.instructor = instructor;
        this.day = day;
        this.time = time;
    }

    public String getCrn() {
        return crn;
    }

    // This allows the Another class to ask "What course is this"
    public Course getCourse() {
        return course;
    }

    public Professor getInstructor() {
        return instructor;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        //This prints: "Section 12345: Algorithms with Dr.Smith[Monday 10:00 AM]"
        return "Section " + crn + ": " + course.getCourseId() + " with " + instructor.getName() + " [" + day + " " + time + "]";
    }


}
