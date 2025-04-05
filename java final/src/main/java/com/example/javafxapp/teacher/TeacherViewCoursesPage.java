package com.example.javafxapp.teacher;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.javafxapp.service.TeacherDataService;
import com.example.javafxapp.service.StudentDataService;
import com.example.javafxapp.model.TeacherData;
import com.example.javafxapp.model.StudentData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeacherViewCoursesPage {
    private Stage window;
    private String currentUser;
    private TeacherDataService teacherDataService;
    private StudentDataService studentDataService;
    private TeacherData teacherData;

    public void show(Stage stage, String user) {
        this.window = stage;
        this.currentUser = user;
        
        //initialize services
        this.teacherDataService = TeacherDataService.getInstance();
        this.studentDataService = StudentDataService.getInstance();
        
        //always get the teacher data regardless of the username provided
        this.teacherData = teacherDataService.getTeacherData("teacher");
        
        if (teacherData == null) {
            //display an error dialog and return to login
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data Error");
            alert.setContentText("Could not load teacher data. Please contact support.");
            alert.showAndWait();
            return;
        }

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: white;");
        container.setPadding(new Insets(20));
        
        Label titleLabel = new Label("My Courses");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        //courses section
        VBox coursesSection = new VBox(10);
        coursesSection.setAlignment(Pos.CENTER_LEFT);
        coursesSection.setPadding(new Insets(20));
        coursesSection.setStyle("-fx-background-color: #F0F1FF; -fx-background-radius: 10;");
        
        Label coursesLabel = new Label("Assigned Courses");
        coursesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //create a list view for courses
        ListView<String> coursesList = new ListView<>();
        coursesList.setPrefHeight(200);
        coursesList.setPrefWidth(400);
        
        //load courses
        List<String> assignedCourses = teacherData.getAssignedCourses();
        ObservableList<String> courses = FXCollections.observableArrayList(assignedCourses);
        coursesList.setItems(courses);
        
        //label for students section
        Label studentsEnrolledLabel = new Label("Students Enrolled:");
        studentsEnrolledLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        //create a table for students in selected course
        TableView<StudentData> studentsTable = new TableView<>();
        studentsTable.setPrefHeight(250);
        studentsTable.setPrefWidth(400);
        
        //set up the columns
        TableColumn<StudentData, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setPrefWidth(100);
        
        TableColumn<StudentData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameColumn.setPrefWidth(150);
        
        TableColumn<StudentData, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setPrefWidth(150);
        
        studentsTable.getColumns().addAll(usernameColumn, nameColumn, emailColumn);
        
        //status label to show selected course info
        Label statusLabel = new Label("Select a course to view enrolled students");
        statusLabel.setStyle("-fx-font-size: 12px;");
        
        //update students table when course is selected
        coursesList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                //get students enrolled in this course
                List<String> enrolledStudents = teacherData.getStudentsInCourse(newVal);
                
                //update status label
                if (enrolledStudents == null || enrolledStudents.isEmpty()) {
                    statusLabel.setText("Course: " + newVal + " - No students enrolled");
                    studentsTable.getItems().clear();
                    return;
                }
                
                statusLabel.setText("Course: " + newVal + " - Students: " + enrolledStudents.size());
                
                //create table data list
                ObservableList<StudentData> studentsInCourse = FXCollections.observableArrayList();
                
                //clear existing items
                studentsTable.getItems().clear();
                
                //process each student
                for (String studentUsername : enrolledStudents) {
                    //get student data
                    StudentData student = studentDataService.getStudentData(studentUsername);
                    
                    if (student != null) {
                        //ensure student has a name
                        if (student.getFullName() == null || student.getFullName().isEmpty()) {
                            student.setFullName("Student " + studentUsername);
                        }
                        
                        //add to list
                        studentsInCourse.add(student);
                    } else {
                        //student not found, create placeholder
                        StudentData tempStudent = new StudentData(studentUsername);
                        tempStudent.setFullName("Unknown Student");
                        tempStudent.setEmail("unknown@school.com");
                        
                        studentsInCourse.add(tempStudent);
                    }
                }
                
                //update the table
                if (!studentsInCourse.isEmpty()) {
                    studentsTable.setItems(studentsInCourse);
                }
            }
        });
        
        coursesSection.getChildren().addAll(
            coursesLabel, coursesList, 
            studentsEnrolledLabel, studentsTable,
            statusLabel
        );
        
        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnAction(e -> new TeacherPage().show(stage, user));
        
        container.getChildren().addAll(titleLabel, coursesSection, backButton);
        
        Scene scene = new Scene(container, 800, 600);
        window.setTitle("Teacher - View Courses");
        window.setScene(scene);
        window.show();
    }
} 