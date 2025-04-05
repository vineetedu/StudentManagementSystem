package com.example.javafxapp.service;

import java.util.Arrays;
import java.util.List;

/**
 * manages the course catalog.
 */
public class CourseCatalogService {
    //singleton instance
    private static CourseCatalogService instance;
    private final List<String> availableCourses;
    
    //private constructor for pattern
    private CourseCatalogService() {
        //initialize with predefined course offerings
        availableCourses = Arrays.asList(
            "MATH101",    //Mathematics
            "CS101",      //Computer Science
            "CS202",      //Advanced Programming
            "PHYS101",    //Physics
            "ENG101",     //English
            "HIST101",    //History
            "GEO101",     //Geography
            "CHEM101",    //Chemistry
            "BIO101",     //Biology
            "ART101",     //Art
            "PE101"       //Physical Education
        );
    }
    
    //get singleton instance
    public static CourseCatalogService getInstance() {
        if (instance == null) {
            instance = new CourseCatalogService();
        }
        return instance;
    }

    public List<String> getAvailableCourses() {
        return availableCourses;
    }
    
    public boolean courseExists(String courseName) {
        return availableCourses.contains(courseName);
    }
} 