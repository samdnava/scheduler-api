package com.sam.scheduler_api.service;

import com.sam.scheduler_api.dto.StudentResponseDTO;
import com.sam.scheduler_api.model.Section;
import com.sam.scheduler_api.model.Student;
import com.sam.scheduler_api.repository.SectionRepository;
import com.sam.scheduler_api.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    // --- 1. DEFINE THE FAKES ---
    // @Mock creates a "hollow" version of the repository.
    // It looks like a repository, but it has no database connection.
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SectionRepository sectionRepository;

    // --- 2. DEFINE THE REAL OBJECT ---
    // @InjectMocks creates a REAL instance of StudentService.
    // However, instead of using real repositories, it plugs in our @Mock fakes from above.
    @InjectMocks
    private StudentService studentService;

    // --- 3. SETUP PHASE ---
    // @BeforeEach means: "Run this method before EVERY single test case."
    // It resets the fakes so they are clean for the next test.
    @BeforeEach
    void setUp() {
        // This command initializes all the @Mock and @InjectMocks annotations.
        MockitoAnnotations.openMocks(this);
    }

    // --- 4. THE TEST CASE ---
    @Test
    // This tells JUnit: "This is a test method. Run it."
    void shouldReturnStudentDTO_WhenRegisteringNewStudent() {

        // --- STEP A: ARRANGE (Prepare the data) ---
        // Create a fake student object to use as input
        Student inputStudent = new Student("123", "Sam", "N", "sam@test.com");

        // Create the object we expect the repository to return
        // (Usually the same as input, but maybe with an ID generated)
        Student savedStudent = new Student("123", "Sam", "N", "sam@test.com");

        // TEACH THE FAKE:
        // "When I call save() with ANY student object, return 'savedStudent' immediately."
        // We do this because the mock has no brain; we have to tell it the answer.
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        // --- STEP B: ACT (Run the actual logic) ---
        // We call the real method on our service
        StudentResponseDTO result = studentService.registerStudent(inputStudent);

        // --- STEP C: ASSERT (Check the results) ---
        // 1. Check that the result is not null (did we get something back?)
        assertNotNull(result);

        // 2. Check that the mapper correctly combined First + Last name
        assertEquals("Sam N", result.fullName());

        // 3. Check that the email matches
        assertEquals("sam@test.com", result.email());

        // 4. Verify that the Service actually called the Repository exactly once.
        // This ensures the service didn't just return a dummy object without saving.
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void shouldEnrollStudent_WhenStudentAndSectionExist() {
        // --- STEP A: ARRANGE (Prepare the Fake World) ---

        // 1. Create the Fake Actors
        // We need a student object.
        Student sam = new Student("123", "Sam", "N", "sam@test.com");

        // 2. Create a Fake Course (So we don't pass null later)
        // We need this because the DTO mapper asks for course.getName()
        com.sam.scheduler_api.model.Course fakeCourse = new com.sam.scheduler_api.model.Course(
                "CSI-101", "Intro to Java", 4.0
        );

        // 3. Create the Section using that Fake Course
        // NOW: section.getCourse() will return our fake object instead of null.
        com.sam.scheduler_api.model.Section javaClass = new com.sam.scheduler_api.model.Section(
                "CRN-101", fakeCourse, null, "Mon/Wed", "10:00 AM"
        );

        // 4. Teach the Mocks their script
        // "Repo, when I ask for Student 123, give me Sam."
        when(studentRepository.findById("123")).thenReturn(Optional.of(sam));

        // "Repo, when I ask for Section CRN-101, give me the Java Class"
        when(sectionRepository.findById("CRN-101")).thenReturn(Optional.of(javaClass));

        // "Repo, when I save Sam, just give him back to me."
        // This simulates the database confirming the save.
        when(studentRepository.save(sam)).thenReturn(sam);

        // --- STEP B: ACT (Run the Logic) ---
        // This is the line we are actually testing.
        StudentResponseDTO response = studentService.enrollStudent("123", "CRN-101");

        // --- STEP C: ASSERT (Check the Results) ---

        // 1. Did we get a result?
        assertNotNull(response);

        // 2. The Big Check: Did the DTO's class list grow?
        // It should now have 1 item in it.
        assertEquals(1, response.enrolledClasses().size());

        // 3. Verify the "Side Effects"
        // Did the service actually try to save Sam to the database?
        // This ensures we didn't just modify the object in memory and forget to save.
        verify(studentRepository, times(1)).save(sam);

    }
}