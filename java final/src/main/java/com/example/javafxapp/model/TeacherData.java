package com.example.javafxapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//manages a teacher's courses, students, grades, and attendance records
public class TeacherData {
    //personal information
    private String username;
    private String fullName;
    private String email;
    
    //teaching assignments and records
    private List<String> assignedCourses;
    private Map<String, List<String>> courseStudents;
    private Map<String, Map<String, Double>> courseGrades;
    private Map<String, Map<String, Boolean>> courseAttendance;

    //create a new teacher profile
    public TeacherData(String username) {
        this.username = username;
        this.assignedCourses = new ArrayList<>();
        this.courseStudents = new HashMap<>();
        this.courseGrades = new HashMap<>();
        this.courseAttendance = new HashMap<>();
    }

    //basic getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    //add a new course to teacher's schedule
    public void addCourse(String course) {
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
            courseStudents.put(course, new ArrayList<>());
            courseGrades.put(course, new HashMap<>());
            courseAttendance.put(course, new HashMap<>());
        }
    }

    //remove a course from teacher's assigned courses
    public void removeCourse(String course) {
        assignedCourses.remove(course);
        courseStudents.remove(course);
        courseGrades.remove(course);
        courseAttendance.remove(course);
    }

    //get list of courses assigned to teacher
    public List<String> getAssignedCourses() {
        return assignedCourses;
    }

    //enroll a student in one of teacher's courses
    public void addStudentToCourse(String course, String studentUsername) {
        //initialize course records if needed
        if (!courseStudents.containsKey(course)) {
            courseStudents.put(course, new ArrayList<>());
            courseGrades.put(course, new HashMap<>());
            courseAttendance.put(course, new HashMap<>());
        }
        
        //add student if not already enrolled
        if (!courseStudents.get(course).contains(studentUsername)) {
            courseStudents.get(course).add(studentUsername);
        }
    }

    //get list of students enrolled in a course
    public List<String> getStudentsInCourse(String course) {
        return courseStudents.getOrDefault(course, new ArrayList<>());
    }

    //get all course-student enrollments
    public Map<String, List<String>> getCourseStudents() {
        return courseStudents;
    }

    //record a student's grade for a course
    public void updateGrade(String course, String studentUsername, double grade) {
        //initialize course gradebook if needed
        if (!courseGrades.containsKey(course)) {
            courseGrades.put(course, new HashMap<>());
        }
        
        //update the grade
        courseGrades.get(course).put(studentUsername, grade);
    }

    //get a student's grade in a course
    public Double getGrade(String course, String studentUsername) {
        if (courseGrades.containsKey(course)) {
            return courseGrades.get(course).getOrDefault(studentUsername, null);
        }
        return null;
    }

    //record student attendance for a course
    public void markAttendance(String course, String studentUsername, boolean present) {
        //initialize course attendance records if needed
        if (!courseAttendance.containsKey(course)) {
            courseAttendance.put(course, new HashMap<>());
        }
        
        //update attendance status
        courseAttendance.get(course).put(studentUsername, present);
    }

    //get a student's attendance status for a course
    public Boolean getAttendance(String course, String studentUsername) {
        if (courseAttendance.containsKey(course)) {
            return courseAttendance.get(course).getOrDefault(studentUsername, null);
        }
        return null;
    }

    //check if a student is enrolled in a course
    public boolean isStudentInCourse(String course, String studentUsername) {
        if (courseStudents.containsKey(course)) {
            return courseStudents.get(course).contains(studentUsername);
        }
        return false;
    }
} 