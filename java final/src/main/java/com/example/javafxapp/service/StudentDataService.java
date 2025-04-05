package com.example.javafxapp.service;

import com.example.javafxapp.model.StudentData;
import com.example.javafxapp.model.Assignment;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * manages student data and academic records
 */
public class StudentDataService {
    //singleton instance
    private static StudentDataService instance;
    private Map<String, StudentData> students;
    private CourseCatalogService courseCatalogService;

    //private constructor for singleton pattern
    private StudentDataService() {
        students = new HashMap<>();
        courseCatalogService = CourseCatalogService.getInstance();
        //initialize with sample data for demonstration
        initializeSampleData();
    }

    //get singleton instance
    public static StudentDataService getInstance() {
        if (instance == null) {
            instance = new StudentDataService();
        }
        return instance;
    }

    //create sample student data for system demonstration
    private void initializeSampleData() {
        //get available courses from catalog
        CourseCatalogService courseCatalog = CourseCatalogService.getInstance();
        List<String> availableCourses = courseCatalog.getAvailableCourses();
        
        //create and configure first student
        StudentData student1 = new StudentData("student1");
        student1.setFullName("Johnny Cash");
        student1.setEmail("johnny.cash@school.edu");
        student1.setPhone("123-456-7890");
        student1.setAddress("123 Cool Street");
        student1.enrollInCourse("CS101");
        student1.enrollInCourse("CS202");
        student1.enrollInCourse("MATH101");
        addAssignmentsForStudent(student1);
        
        //create and configure second student
        StudentData student2 = new StudentData("student2");
        student2.setFullName("Elvis Presley");
        student2.setEmail("elvis.presley@school.edu");
        student2.setPhone("987-654-3210");
        student2.setAddress("456 Disco Ave");
        student2.enrollInCourse("CS101");
        student2.enrollInCourse("BIO101");
        student2.enrollInCourse("MATH101");
        addAssignmentsForStudent(student2);
        
        //create and configure third student
        StudentData student3 = new StudentData("student3");
        student3.setFullName("Michael Jackson");
        student3.setEmail("michael.jackson@school.edu");
        student3.setPhone("123-654-7890");
        student3.setAddress("123 Dance Street");
        student3.enrollInCourse("CS202");
        student3.enrollInCourse("BIO101");
        student3.enrollInCourse("HIST101");
        addAssignmentsForStudent(student3);
        
        //store students in the system
        students.put("student1", student1);
        students.put("student2", student2);
        students.put("student3", student3);
    }

    //add sample assignments for a student's enrolled courses
    private void addAssignmentsForStudent(StudentData student) {
        List<String> courses = student.getEnrolledCourses();
        
        if (courses == null || courses.isEmpty()) {
            return;
        }
        
        for (String course : courses) {
            //create assignments with different states for demonstration
            Assignment assignment1 = new Assignment(course, "Homework 1", 
                                                  "Complete exercises 1-10", "04/01/2024");
            assignment1.setSubmitted(true);
            assignment1.setSubmissionDate("04/01/2025");
            assignment1.setGraded(true);
            assignment1.setScore(90.0); // High score for student1, medium for others
            if (!student.getUsername().equals("student1")) {
                assignment1.setScore(80.0);
            }
            
            Assignment assignment2 = new Assignment(course, "Project Proposal", 
                                                  "Submit your project proposal", "04/10/2025");
            assignment2.setSubmitted(true);
            assignment2.setSubmissionDate("04/09/2025");
            
            Assignment assignment3 = new Assignment(course, "Final Project", 
                                                  "Submit your final project", "05/15/2025");
            
            //add assignments to student's record
            student.addAssignment(assignment1);
            student.addAssignment(assignment2);
            student.addAssignment(assignment3);
        }
    }

    //retrieve student's academic record
    public StudentData getStudentData(String username) {
        return students.get(username);
    }

    //update student's academic record
    public void updateStudentData(StudentData studentData) {
        students.put(studentData.getUsername(), studentData);
    }

    //enroll student in a new course
    public void enrollStudentInCourse(String username, String course) {
        StudentData student = students.get(username);
        if (student != null && courseCatalogService.courseExists(course)) {
            student.enrollInCourse(course);
            
            //create initial assignment for the new course
            Assignment newAssignment = new Assignment(
                course,
                "Introduction to " + course,
                "Complete the introductory assignment for " + course,
                getFormattedFutureDate(14) //due in 2 weeks
            );
            student.addAssignment(newAssignment);
        }
    }

    //add a new assignment to student's record
    public void addAssignment(String username, Assignment assignment) {
        StudentData student = students.get(username);
        if (student != null) {
            student.addAssignment(assignment);
        }
    }

    //get all student records
    public Map<String, StudentData> getAllStudents() {
        return new HashMap<>(students); //return copy
    }
    
    //generate a future date string for assignment due dates
    private String getFormattedFutureDate(int daysInFuture) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(java.util.Calendar.DAY_OF_MONTH, daysInFuture);
        return sdf.format(calendar.getTime());
    }
} 