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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        Student newStudent = new Student(null, "Integration", "Test", "test@test.com");

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
    public void shouldUnenrollStudent_WhenEnrolled() throws Exception {
        // 1. ARRANGE
        Student student = new Student(null, "Drop", "Class", "drop.class@test.com");
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

    @Test
    public void shouldReturn400_WhenSectionIsFull() throws Exception {
        // 1. ARRANGE
        // Create two students
        Student s1 = new Student(null, "Student", "One", "s1@test.com");
        String id1 = studentService.registerStudent(s1).id();

        Student s2 = new Student(null, "Student", "Two", "s2@test.com");
        String id2 = studentService.registerStudent(s2).id();

        Course course = new Course("PHY-101", "Physics", 3.0);
        courseService.saveCourse(course);

        // Create a Section with Capacity = 1
        Section section = new Section("CRN-FULL", course, null, "Fri", "2:00 PM");
        section.setCapacity(1);
        sectionService.saveSection(section);

        // Enroll Student 1 (Success - now the class is full)
        studentService.enrollStudent(id1, "CRN-FULL");

        // 2. ACT & ASSERT
        // Try to enroll Student 2 -> Should fail with 400 Bad Request
        mockMvc.perform(post("/students/" + id2 + "/enroll/CRN-FULL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // Expect HTTP 400
    }

    @Test
    public void shouldReturnSchedule_WhenEnrolled() throws Exception {
        // 1. ARRANGE
        Student student = new Student(null, "Schedule", "Check", "schedule@test.com");
        String studentId = studentService.registerStudent(student).id();

        Course course = new Course("BIO-101", "Biology", 4.0);
        courseService.saveCourse(course);

        // Create a dummy professor
        Professor professor = new Professor("prof-bio", "Dr. Darwin", "Biology");
        professorService.saveProfessor(professor);

        // Create a specific section
        Section section = new Section("CRN-BIO", course, professor, "Mon/Fri", "11:00 AM");
        sectionService.saveSection(section);

        // Enroll the student
        studentService.enrollStudent(studentId, "CRN-BIO");

        // 2. ACT & ASSERT
        // We call the NEW endpoint and check if it contains the course name "Biology"
        mockMvc.perform(get("/students/" + studentId + "/schedule")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName").value("Biology"))
                .andExpect(jsonPath("$[0].professorName").value("Dr. Darwin"))
                .andExpect(jsonPath("$[0].timeOfDay").value("11:00 AM"));
    }

    @Test
    public void shouldReturn409_WhenTimeConflictOccurs() throws Exception {
        // 1. ARRANGE
        Student sam = new Student(null, "Busy", "Student", "busy@test.com");
        String studentId = studentService.registerStudent(sam).id();

        Course bio = new Course("BIO-101", "Biology", 4.0);
        courseService.saveCourse(bio);

        Course hist = new Course("HIST-101", "History", 3.0);
        courseService.saveCourse(hist);

        // Class 1: Monday at 10:00 AM
        Section section1 = new Section("CRN-BIO", bio, null, "Monday", "10:00 AM");
        sectionService.saveSection(section1);

        // Class 2: Monday at 10:00 AM (Conflict!)
        Section section2 = new Section("CRN-HIST", hist, null, "Monday", "10:00 AM");
        sectionService.saveSection(section2);

        // Enroll in Class 1 (Success)
        studentService.enrollStudent(studentId, "CRN-BIO");

        // 2. ACT & ASSERT
        // Try to enroll in Class 2 -> Should fail with 409 Conflict
        mockMvc.perform(post("/students/" + studentId + "/enroll/CRN-HIST")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
}
