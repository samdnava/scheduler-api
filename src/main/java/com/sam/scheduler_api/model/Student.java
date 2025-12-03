package com.sam.scheduler_api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Course> transcript;
    private List<Section> currentSchedule;

    public Student(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.transcript = new ArrayList<>();
        this.currentSchedule = new ArrayList<>();

    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<Course> getTranscript() {
        return transcript;
    }

    public List<Section> getCurrentSchedule() {
        return currentSchedule;
    }

    public void addCompletedCourse(Course course) {
        if (!transcript.contains(course)) {
            transcript.add(course);
        }
    }

    public void registerForSection(Section newSection) {
        // Step 1: Run the Prereq Check
        // We get the Course object from the Section object to check its rules
        boolean isElegible = checkPrerequisites(newSection.getCourse());

        if (!isElegible) {
            System.out.println("Registration Failed: Prerequisites not met for " + newSection.getCourse().getName());
            return; // STOP HERE. Do not add to schedule
        }


        // Step 2: Check for duplicates (The code you wrote yesterday)
        if (!currentSchedule.contains(newSection)) {
            currentSchedule.add(newSection);
            System.out.println("Success: Registered for " + newSection);
        } else {
            // 2. Give feedback if it failed
            System.out.println("Failed: Already Registered for " + newSection);
        }
    }

    public void dropSection(Section sectionToDrop) {
        currentSchedule.remove(sectionToDrop);
        System.out.println("Success: Dropped " + sectionToDrop);
    }

    public boolean checkPrerequisites(Course targetCourse) {
        // 1. Get the list of what is required
        List<Course> required = targetCourse.getPreRequisites();

        // 2. Loop through every requirement
        for (Course req : required) {
            // 3. Check if we have taken it
            if (!transcript.contains(req)) {
                // If we are missing even ONE, we fail.
                System.out.println("Prereq Check Failed: Missing " + req.getName());
                return false;
            }
        }
        // 4. If we survived the loop we have everything.
        return true;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
