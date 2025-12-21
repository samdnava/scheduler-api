package com.sam.scheduler_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

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

    // --- RELATIONSHIP: MANY-TO-MANY ---
    // This tells the DB: "One student has many sections, and one section has many students."
    // We use Set (instead of List) to prevent a student from enrolling in the same class twice.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "enrollments", // This creates a middle table called 'enrollments'
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "section_crn")
    )
    private Set<Section> sections = new HashSet<>();

    public Set<Section> getSections() {
        return sections;
    }

    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }

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

}
