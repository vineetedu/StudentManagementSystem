package com.example.javafxapp.model;

//holds summary information about a course's performance metrics
public class CourseSummary {
    //course details
    private String courseName;
    private String completedAssignments;
    
    //create a new course summary
    public CourseSummary(String courseName, String completedAssignments) {
        this.courseName = courseName;
        this.completedAssignments = completedAssignments;
    }
    
    //basic getters and setters
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getCompletedAssignments() { return completedAssignments; }
    public void setCompletedAssignments(String completedAssignments) { this.completedAssignments = completedAssignments; }
} 