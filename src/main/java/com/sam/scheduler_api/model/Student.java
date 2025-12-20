package com.sam.scheduler_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // @NotBlank: Checks that the text is not null and length > 0
    @NotBlank(message = "First name is mandatory")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Email should be valid")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    // @Email: Uses a standard pattern to check for "text@domain.com"
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Column(name = "email", nullable = false, unique = true)
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

