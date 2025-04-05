package com.example.javafxapp.student;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.javafxapp.service.StudentDataService;
import com.example.javafxapp.service.CourseCatalogService;
import com.example.javafxapp.model.StudentData;

/**
 * page for students to browse and enroll in available courses.
 */
public class StudentEnrollCoursesPage {
    private Stage mainWindow;
    private String studentUsername;
    private StudentDataService dataService;
    private CourseCatalogService courseCatalogService;
    private StudentData studentData;
    private ListView<String> courseList;

    public void show(Stage stage, String user) {
        this.mainWindow = stage;
        this.studentUsername = user;
        this.dataService = StudentDataService.getInstance();
        this.courseCatalogService = CourseCatalogService.getInstance();
        this.studentData = dataService.getStudentData(user);

        //setup main container
        VBox pageContainer = new VBox(20);
        pageContainer.setAlignment(Pos.CENTER);
        pageContainer.setStyle("-fx-background-color: white;");
        pageContainer.setPadding(new Insets(20));
        
        //add page title
        Label pageTitle = new Label("Course Enrollment");
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        //create courses section
        VBox coursesSection = new VBox(10);
        coursesSection.setAlignment(Pos.CENTER_LEFT);
        coursesSection.setPadding(new Insets(20));
        coursesSection.setStyle("-fx-background-color: #F0F1FF; -fx-background-radius: 10;");
        
        //add section headers
        Label availableCoursesLabel = new Label("Available Courses");
        availableCoursesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //setup available courses list
        courseList = new ListView<>();
        courseList.setPrefHeight(300);
        courseList.setPrefWidth(400);
        
        //load available courses
        ObservableList<String> availableCourses = FXCollections.observableArrayList();
        for (String course : courseCatalogService.getAvailableCourses()) {
            if (!studentData.getEnrolledCourses().contains(course)) {
                availableCourses.add(course);
            }
        }
        courseList.setItems(availableCourses);
        
        //setup enrolled courses section
        Label enrolledCoursesLabel = new Label("Currently Enrolled Courses");
        enrolledCoursesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //setup enrolled courses list
        ListView<String> enrolledList = new ListView<>();
        enrolledList.setPrefHeight(150);
        enrolledList.setPrefWidth(400);
        
        //load enrolled courses
        ObservableList<String> enrolledCourses = FXCollections.observableArrayList(studentData.getEnrolledCourses());
        enrolledList.setItems(enrolledCourses);
        
        //create enroll button
        Button enrollButton = new Button("Enroll in Selected Course");
        enrollButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        enrollButton.setOnMouseEntered(e -> enrollButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        enrollButton.setOnMouseExited(e -> enrollButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        enrollButton.setOnAction(e -> handleEnrollment(availableCourses, enrolledCourses));
        
        //add all elements to courses section
        coursesSection.getChildren().addAll(
            availableCoursesLabel, courseList, 
            enrollButton,
            new Separator(),
            enrolledCoursesLabel, enrolledList
        );
        
        //create back button
        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnAction(e -> new StudentPage().show(stage, user));
        
        //combine all elements
        pageContainer.getChildren().addAll(pageTitle, coursesSection, backButton);
        
        //show the page
        Scene pageScene = new Scene(pageContainer, 800, 600);
        mainWindow.setTitle("Student - Enroll in Courses");
        mainWindow.setScene(pageScene);
        mainWindow.show();
    }

    private void handleEnrollment(ObservableList<String> availableCourses, ObservableList<String> enrolledCourses) {
        String selectedCourse = courseList.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            //enroll in the course
            dataService.enrollStudentInCourse(studentUsername, selectedCourse);
            
            //update course lists
            enrolledCourses.add(selectedCourse);
            availableCourses.remove(selectedCourse);
            
            //show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully enrolled in " + selectedCourse);
            alert.showAndWait();
        } else {
            //show warning if no course selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a course to enroll in");
            alert.showAndWait();
        }
    }
} 