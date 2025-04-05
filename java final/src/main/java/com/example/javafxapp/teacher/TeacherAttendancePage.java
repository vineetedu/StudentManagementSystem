package com.example.javafxapp.teacher;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.javafxapp.service.TeacherDataService;
import com.example.javafxapp.service.StudentDataService;
import com.example.javafxapp.model.TeacherData;
import com.example.javafxapp.model.StudentData;
import java.util.List;

public class TeacherAttendancePage {
    private Stage window;
    private String currentUser;
    private TeacherDataService teacherDataService;
    private StudentDataService studentDataService;
    private TeacherData teacherData;

    public void show(Stage stage, String user) {
        this.window = stage;
        this.currentUser = user;
        this.teacherDataService = TeacherDataService.getInstance();
        this.studentDataService = StudentDataService.getInstance();
        
        //default/set teacher name
        this.teacherData = teacherDataService.getTeacherData("teacher");
        
        if (teacherData == null) {
            //error
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
        
        Label titleLabel = new Label("Report Attendance");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        //attendance section
        VBox attendanceSection = new VBox(10);
        attendanceSection.setAlignment(Pos.CENTER_LEFT);
        attendanceSection.setPadding(new Insets(20));
        attendanceSection.setStyle("-fx-background-color: #F0F1FF; -fx-background-radius: 10;");
        
        //course selection
        Label courseLabel = new Label("Select Course:");
        courseLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        //course combo box
        ComboBox<String> courseComboBox = new ComboBox<>();
        courseComboBox.setPrefWidth(300);
        courseComboBox.setItems(FXCollections.observableArrayList(teacherData.getAssignedCourses()));
        
        //student label
        Label studentLabel = new Label("Select Student:");
        studentLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        ComboBox<String> studentComboBox = new ComboBox<>();
        studentComboBox.setPrefWidth(300);
        
        //attendance date
        Label dateLabel = new Label("Date (MM/DD/YYYY):");
        dateLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        TextField dateField = new TextField();
        dateField.setPrefWidth(300);
        dateField.setText(getCurrentDate());
        
        //attendance status
        Label statusLabel = new Label("Attendance Status:");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.setPrefWidth(300);
        statusComboBox.setItems(FXCollections.observableArrayList("Present", "Absent"));
        statusComboBox.getSelectionModel().select("Present");
        
        //update student list when course is selected
        courseComboBox.setOnAction(e -> {
            String selectedCourse = courseComboBox.getValue();
            if (selectedCourse != null) {
                List<String> studentsInCourse = teacherData.getStudentsInCourse(selectedCourse);
                
                if (studentsInCourse == null || studentsInCourse.isEmpty()) {
                    studentComboBox.setItems(FXCollections.observableArrayList());
                    studentComboBox.setDisable(true);
                } else {
                    ObservableList<String> displayNames = FXCollections.observableArrayList();
                    for (String student : studentsInCourse) {
                        StudentData studentData = studentDataService.getStudentData(student);
                        if (studentData != null) {
                            String displayName = student + " (" + studentData.getFullName() + ")";
                            displayNames.add(displayName);
                        }
                    }
                    studentComboBox.setItems(displayNames);
                    studentComboBox.setDisable(false);
                }
            }
        });
        
        Button saveButton = new Button("Save Attendance");
        saveButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        saveButton.setOnAction(e -> {
            //validate inputs
            String selectedCourse = courseComboBox.getValue();
            String selectedDisplayName = studentComboBox.getValue();
            String date = dateField.getText();
            String status = statusComboBox.getValue();
            
            if (selectedCourse == null || selectedDisplayName == null || date.isEmpty() || status == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR); //error
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill out all fields!");
                alert.showAndWait();
                return;
            }
            
            //validate date format (MM/DD/YYYY)
            if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter date in MM/DD/YYYY format!");
                alert.showAndWait();
                return;
            }
            
            //convert date format
            String[] dateParts = date.split("/");
            String formattedDate = dateParts[2] + "-" + dateParts[0] + "-" + dateParts[1];
            
            //extract username from display name
            String selectedStudent = selectedDisplayName.split(" \\(")[0];
            
            //mark attendance
            boolean present = "Present".equals(status);
            saveAttendanceRecord(selectedCourse, selectedStudent, formattedDate, present);
            
            //success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Attendance has been recorded successfully!");
            alert.showAndWait();
        });
        
        attendanceSection.getChildren().addAll(courseLabel, courseComboBox, studentLabel, studentComboBox, dateLabel, dateField, statusLabel, statusComboBox, saveButton);
        
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnAction(e -> new TeacherPage().show(stage, user));
        
        container.getChildren().addAll(titleLabel, attendanceSection, backButton);
        
        Scene scene = new Scene(container, 800, 600);
        window.setTitle("Teacher - Attendance");
        window.setScene(scene);
        window.show();
    }
    
    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(new java.util.Date());
    }
    
    //save attendance
    private void saveAttendanceRecord(String course, String studentUsername, String date, boolean present) {
        //use the TeacherDataService to save attendance
        teacherDataService.markStudentAttendanceWithDate(currentUser, course, studentUsername, date, present);
    }
} 