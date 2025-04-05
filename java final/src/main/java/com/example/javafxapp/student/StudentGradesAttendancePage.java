package com.example.javafxapp.student;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.javafxapp.service.StudentDataService;
import com.example.javafxapp.model.StudentData;
import com.example.javafxapp.model.Assignment;
import com.example.javafxapp.model.AttendanceRecord;
import java.util.List;
import java.util.ArrayList;

public class StudentGradesAttendancePage {
    private Stage window;
    private String currentUser;
    private StudentDataService studentDataService;
    private StudentData studentData;

    public void show(Stage stage, String user) {
        this.window = stage;
        this.currentUser = user;
        
        //initialize services
        this.studentDataService = StudentDataService.getInstance();
        this.studentData = studentDataService.getStudentData(user);
        
        System.out.println("Loading grades and attendance for student: " + user);
        if (studentData == null) {
            System.out.println("ERROR: Student data is null for " + user);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data Error");
            alert.setContentText("Could not load student data. Please contact support.");
            alert.showAndWait();
            return;
        }
        
        if (studentData.getFullName() == null || studentData.getFullName().isEmpty()) {
            System.out.println("WARNING: Student " + studentData.getUsername() + " has no name, setting default");
            studentData.setFullName("Student " + studentData.getUsername());
        }
        
        System.out.println("Student: " + studentData.getFullName() + 
                         " - Courses: " + studentData.getEnrolledCourses().size());

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: white;");
        container.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Grades and Attendance");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        //grades vbox
        VBox gradesSection = createGradesSection();
        
        //attendance vbox
        VBox attendanceSection = createAttendanceSection();
        
        //back button
        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnAction(e -> new StudentPage().show(stage, user));
        
        container.getChildren().addAll(titleLabel, gradesSection, attendanceSection, backButton);
        
        Scene scene = new Scene(container, 800, 600);
        window.setTitle("Student - Grades and Attendance");
        window.setScene(scene);
        window.show();
    }
    
    //creates the grades section with table
    private VBox createGradesSection() {
        VBox gradesSection = new VBox(10);
        gradesSection.setAlignment(Pos.CENTER_LEFT);
        gradesSection.setPadding(new Insets(20));
        gradesSection.setStyle("-fx-background-color: #F0F1FF; -fx-background-radius: 10;");
        
        Label gradesLabel = new Label("Course Grades");
        gradesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //create table for grades
        TableView<GradeEntry> gradesTable = new TableView<>();
        gradesTable.setPrefHeight(200);
        
        //set up columns
        TableColumn<GradeEntry, String> courseColumn = new TableColumn<>("Course");
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        courseColumn.setPrefWidth(200);
        
        TableColumn<GradeEntry, String> assignmentColumn = new TableColumn<>("Assignment");
        assignmentColumn.setCellValueFactory(new PropertyValueFactory<>("assignment"));
        assignmentColumn.setPrefWidth(200);
        
        TableColumn<GradeEntry, String> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeColumn.setPrefWidth(100);
        gradeColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        //add columns to table
        gradesTable.getColumns().addAll(courseColumn, assignmentColumn, gradeColumn);
        
        //load grade data
        ObservableList<GradeEntry> grades = loadGradeData();
        gradesTable.setItems(grades);
        
        //add components to section
        gradesSection.getChildren().addAll(gradesLabel, gradesTable);
        return gradesSection;
    }
    
    //creates the attendance section with table
    private VBox createAttendanceSection() {
        VBox attendanceSection = new VBox(10);
        attendanceSection.setAlignment(Pos.CENTER_LEFT);
        attendanceSection.setPadding(new Insets(20));
        attendanceSection.setStyle("-fx-background-color: #F0F1FF; -fx-background-radius: 10;");
        
        Label attendanceLabel = new Label("Attendance Records");
        attendanceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //create table for attendance
        TableView<AttendanceRecord> attendanceTable = new TableView<>();
        attendanceTable.setPrefHeight(200);
        
        //set up columns
        TableColumn<AttendanceRecord, String> courseColumn = new TableColumn<>("Course");
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        courseColumn.setPrefWidth(200);
        
        TableColumn<AttendanceRecord, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setPrefWidth(150);
        
        TableColumn<AttendanceRecord, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(100);
        statusColumn.setCellFactory(column -> {
            return new TableCell<AttendanceRecord, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if (item.equals("Present")) {
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                        } else {
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        }
                    }
                }
            };
        });
        
        //add columns to table
        attendanceTable.getColumns().addAll(courseColumn, dateColumn, statusColumn);
        
        //load attendance data
        ObservableList<AttendanceRecord> attendanceRecords = loadAttendanceData();
        attendanceTable.setItems(attendanceRecords);
        
        //add components to section
        attendanceSection.getChildren().addAll(attendanceLabel, attendanceTable);
        return attendanceSection;
    }
    
    //load grade data from student data
    private ObservableList<GradeEntry> loadGradeData() {
        ObservableList<GradeEntry> grades = FXCollections.observableArrayList();
        
        List<Assignment> assignments = studentData.getAssignments();
        
        if (assignments == null || assignments.isEmpty()) {
            grades.add(new GradeEntry("No courses enrolled", "No assignments", "N/A"));
            return grades;
        }
        
        for (Assignment assignment : assignments) {
            if (assignment.isGraded()) {
                String formattedGrade = String.format("%.1f", assignment.getScore());
                grades.add(new GradeEntry(
                    assignment.getCourse(),
                    assignment.getTitle(),
                    formattedGrade
                ));
            }
        }
        
        if (grades.isEmpty()) {
            grades.add(new GradeEntry("No graded assignments", "", "N/A"));
        }
        
        return grades;
    }
    
    //load attendance data from stored records
    private ObservableList<AttendanceRecord> loadAttendanceData() {
        ObservableList<AttendanceRecord> records = FXCollections.observableArrayList();
        
        List<AttendanceRecord> storedRecords = studentData.getAttendanceRecords();
        
        if (storedRecords != null && !storedRecords.isEmpty()) {
            records.addAll(storedRecords);
        } else {
            records.add(new AttendanceRecord("No attendance records", "N/A", "N/A"));
        }
        
        return records;
    }
    
    //grade entry for the grades table
    public static class GradeEntry {
        private final String course;
        private final String assignment;
        private final String grade;
        
        public GradeEntry(String course, String assignment, String grade) {
            this.course = course;
            this.assignment = assignment;
            this.grade = grade;
        }
        
        public String getCourse() { return course; }
        public String getAssignment() { return assignment; }
        public String getGrade() { return grade; }
    }
} 