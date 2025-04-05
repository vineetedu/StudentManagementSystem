package com.example.javafxapp.model;

//represents a student assignment with its details and status
public class Assignment {
    //basic assignment info
    private String course;
    private String title;
    private String description;
    private String dueDate;
    
    //assignment status
    private boolean submitted;
    private boolean graded;
    private double score;
    private String submissionDate;

    //create a new assignment
    public Assignment(String course, String title, String description, String dueDate) {
        //set basic info
        this.course = course;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        
        //initialize status
        this.submitted = false;
        this.graded = false;
        this.score = 0.0;
        this.submissionDate = "";
    }

    //basic getters and setters
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    //status getters and setters
    public boolean isSubmitted() { return submitted; }
    public void setSubmitted(boolean submitted) { this.submitted = submitted; }
    
    public boolean isGraded() { return graded; }
    public void setGraded(boolean graded) { this.graded = graded; }
    
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    
    public String getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(String submissionDate) { this.submissionDate = submissionDate; }
    
    //format assignment for display in lists
    @Override
    public String toString() {
        return title + " (" + course + ")";
    }
} 