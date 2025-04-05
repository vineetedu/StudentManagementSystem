package com.example.javafxapp.model;

import java.util.ArrayList;
import java.util.List;

//represents a student's complete academic profile and records
public class StudentData {
    //personal information
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    
    //academic records
    private List<String> enrolledCourses;
    private List<Assignment> assignments;
    private List<AttendanceRecord> attendanceRecords;

    //create a new student profile
    public StudentData(String username) {
        this.username = username;
        this.enrolledCourses = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.attendanceRecords = new ArrayList<>();
    }

    //basic getters and setters
    public String getUsername() { return username; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    //academic record accessors
    public List<String> getEnrolledCourses() { return enrolledCourses; }
    public List<Assignment> getAssignments() { return assignments; }
    public List<AttendanceRecord> getAttendanceRecords() { return attendanceRecords; }

    //enroll student in a new course
    public void enrollInCourse(String course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }
    
    //add a new assignment to student's record
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }
    
    //add an attendance record
    public void addAttendanceRecord(AttendanceRecord record) {
        attendanceRecords.add(record);
    }
    
    //create and add a new attendance record
    public void addAttendanceRecord(String course, String date, boolean present) {
        String status = present ? "Present" : "Absent";
        attendanceRecords.add(new AttendanceRecord(course, date, status));
    }
} 