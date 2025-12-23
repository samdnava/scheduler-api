package com.sam.scheduler_api.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sections")
public class Section {

    @Id
    @Column(length = 10)
    private String crn; // Primary Key (e.g., "101")

    // --- RELATIONSHIPS ---

    // ORM Magic: Instead of storing a string "CS101", we store the full Course object.
    @ManyToOne
    @JoinColumn(name = "course_id") // This creates the Foreign Key column in the DB
    private Course course;

    @ManyToOne
    @JoinColumn(name = "professor_id") // This creates the Foreign Key column in the DB
    private Professor instructor;

    // --- BASIC FIELDS ---
    private String dayOfWeek; // e.g., "Monday"
    private String timeOfDay; // e.g., "10:00 AM"
    private int capacity = 30; // Default capacity of 30
    // This makes the relationship Bidirectional.
    // "mappedBy" tells Hibernate: "Go look at the 'sections' field in the Student class to figure out how to join these."
    @ManyToMany(mappedBy = "sections")
    private Set<Student> students = new HashSet<>();

    // --- CONSTRUCTORS ---
    public Section() {
    }

    public Section(String crn, Course course, Professor instructor, String dayOfWeek, String timeOfDay) {
        this.crn = crn;
        this.course = course;
        this.instructor = instructor;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
    }

    // --- GETTERS AND SETTERS ---

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Professor getInstructor() {
        return instructor;
    }

    public void setInstructor(Professor instructor) {
        this.instructor = instructor;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        //This prints: "Section 12345: Algorithms with Dr.Smith[Monday 10:00 AM]"
        return "Section " + crn + ": " + course.getCourseId() + " with " + instructor.getName() + " [" + dayOfWeek + " " + timeOfDay + "]";
    }
}
