package com.example.javafxapp.model;

//stores a single attendance record for a student in a course
public class AttendanceRecord {
    //attendance details
    private String course;
    private String date;
    private String status;  // can be "Present" or "Absent"
    
    //create a new attendance record
    public AttendanceRecord(String course, String date, String status) {
        this.course = course;
        this.date = date;
        this.status = status;
    }
    
    //basic getters and setters
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 