package com.example.javafxapp.model;

public class CourseEntry {
    private String course;
    private String studentCount;
    private String studentList;

    public CourseEntry(String course, String studentCount, String studentList) {
        this.course = course;
        this.studentCount = studentCount;
        this.studentList = studentList;
    }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getStudentCount() { return studentCount; }
    public void setStudentCount(String studentCount) { this.studentCount = studentCount; }

    public String getStudentList() { return studentList; }
    public void setStudentList(String studentList) { this.studentList = studentList; }
} 