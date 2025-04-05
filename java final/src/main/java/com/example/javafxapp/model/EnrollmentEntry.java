package com.example.javafxapp.model;

public class EnrollmentEntry {
    private String studentName;
    private String username;
    private String enrolledCourses;

    public EnrollmentEntry(String studentName, String username, String enrolledCourses) {
        this.studentName = studentName;
        this.username = username;
        this.enrolledCourses = enrolledCourses;
    }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEnrolledCourses() { return enrolledCourses; }
    public void setEnrolledCourses(String enrolledCourses) { this.enrolledCourses = enrolledCourses; }
} 