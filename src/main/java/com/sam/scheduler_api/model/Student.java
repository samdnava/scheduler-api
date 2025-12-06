package com.sam.scheduler_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(length = 20) // Equivalent to VARCHAR(20)
    private String id;

    @Column(nullable = false) // Equivalent to NOT NULL
    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false) // Unique constraint
    private String email;

    // --- 1. JPA REQUIRES AN EMPTY CONSTRUCTOR ---
    public Student() {
    }

    // --- 2. OUR CONSTRUCTOR (For us to use) ---
    public Student(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    // --- 3. GETTERS AND SETTERS ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // --- NEW: The Schedule ---
    // @ManyToMany: Tells the DB that Students and Sections have a complex relationship
    // @JoinTable: Automatically creates the "enrollments" middle table for us
    @ManyToMany
    @JoinTable(
            name = "enrollments",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "section_crn")
    )
    private List<Section> schedule = new ArrayList<>();

    // Getter and Setter for the list
    public List<Section> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Section> schedule) {
        this.schedule = schedule;
    }
}

