package com.sam.scheduler_api.integration;

import com.sam.scheduler_api.dto.StudentResponseDTO;
import tools.jackson.databind.ObjectMapper;
import com.sam.scheduler_api.model.Course;
import com.sam.scheduler_api.model.Professor;
import com.sam.scheduler_api.model.Section;
import com.sam.scheduler_api.model.Student;
import com.sam.scheduler_api.service.CourseService;
import com.sam.scheduler_api.service.ProfessorService;
import com.sam.scheduler_api.service.SectionService;
import com.sam.scheduler_api.service.StudentService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// These static imports allow us to use post() and jsonPath() directly
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StudentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // -- INJECT SERVICES --
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private ProfessorService professorService;

    @Test
    public void shouldCreateStudent_EndToEnd() throws Exception {
        // 1. ARRANGE
        Student newStudent = new Student("999", "Integration", "Test", "test@test.com");

        // 2. ACT & ASSERT
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Integration Test"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    public void shouldFailWhenFirstNameIsBlank() throws Exception {
        // 1. ARRANGE - Create a student with an empty First Name
        Student invalidStudent = new Student(null, "", "Test", "test@test.com");

        // 2. ACT & ASSERT
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest()); // We expect HTTP 400 Error
    }

    @Test
    public void shouldEnrollStudent_EndToEnd() throws Exception {
        // 1. ARRANGE: Create the "World"
        // We need a Student, a Course, a Professor, and a Section in the DB first.
        Student sam = new Student(null, "Sam", "Test", "test@test.com");
        studentService.registerStudent(sam);

        // CAPTURE the saved DTO which contains the real, generated UUID
        StudentResponseDTO savedStudent = studentService.registerStudent(sam);
        String generatedId = savedStudent.id();

        Course javaCourse = new Course("CSI-101", "Intro to Java", 4.0);
        courseService.saveCourse(javaCourse);

        Professor prof = new Professor("prof-1", "Dr.Smith", "Computer Science");
        professorService.saveProfessor(prof);

        Section section = new Section("CRN-9999", javaCourse, prof, "Mon/Wed", "10:00 AM");
        sectionService.saveSection(section);

        // 2. ACT: Call the Enroll Endpoint
        mockMvc.perform(post("/students/" + generatedId + "/enroll/CRN-9999")
                        .contentType(MediaType.APPLICATION_JSON))
                // 3. ASSERT: Call the Enroll Endpoint
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrolledClasses[0]").value("Intro to Java"));
    }

    @Test
    public void shouldReturn404_WhenStudentNotFound() throws Exception {
        // ACT & ASSERT
        // We try to enroll a student with ID "non-existen-id" which definitely isn't in the DB.
        mockMvc.perform(post("/students/non-existent-id/enroll/CRN-9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // We expect a 404 error, not 200 or 500
    }

    @Test
    public void shouldReturn409_WhenStudentAlreadyEnrolled() throws Exception {
        // 1. ARRANGE
        Student student = new Student(null, "Double", "Entry", "double@test.com");
        StudentResponseDTO savedStudent = studentService.registerStudent(student);
        String studentId = savedStudent.id();

        Course course = new Course("MATH-101", "Calculus", 4.0);
        courseService.saveCourse(course);

        // Use a unique CRN for this test to avoid conflicts with other tests
        Section section = new Section("CRN-DUPE", course, null, "Tue/Thu", "2:00");
        sectionService.saveSection(section);

        // Enroll once (Success)
        studentService.enrollStudent(studentId, "CRN-DUPE");

        // 2. ACT & ASSERT (Enroll again -> Expect Failure
        mockMvc.perform(post("/students/" + studentId + "/enroll/CRN-DUPE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void  shouldUnenrollStudent_WhenEnrolled() throws Exception {
        // 1. ARRANGE
        Student student = new Student (null, "Drop", "Class", "drop.class@test.com");
        StudentResponseDTO savedStudent = studentService.registerStudent(student);
        String studentId = savedStudent.id();

        Course course = new Course("HIST-101", "History", 3.0);
        courseService.saveCourse(course);

        // Unique CRN
        Section section = new Section("CRN-DROP", course, null, "Fri", "9:00AM");
        sectionService.saveSection(section);

        // Enroll first (Setup)
        studentService.enrollStudent(studentId, "CRN-DROP");

        // 2. ACT & ASSERT (Unenroll -> Expect 204 No Content)
        mockMvc.perform(delete("/students/" + studentId + "/enroll/CRN-DROP"))
                .andExpect(status().isNoContent());
    }

}
