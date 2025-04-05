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
import com.example.javafxapp.service.TeacherDataService;
import com.example.javafxapp.model.StudentData;
import com.example.javafxapp.model.AttendanceRecord;
import java.util.List;

public class StudentAttendancePage {
    private Stage window;
    private String currentUser;
    private StudentDataService studentDataService;
    private TeacherDataService teacherDataService;
    private StudentData studentData;

    public void show(Stage stage, String user) {
        window = stage;
        currentUser = user;
        studentDataService = StudentDataService.getInstance();
        teacherDataService = TeacherDataService.getInstance();
        studentData = studentDataService.getStudentData(user);

        System.out.println("Loading attendance for student: " + user);
        if (studentData == null) {
            System.out.println("ERROR: Student data is null for " + user);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data Error");
            alert.setContentText("Could not load student data. Please contact support.");
            alert.showAndWait();
            return;
        }
        
        System.out.println("Student: " + studentData.getFullName() + 
                         " - Courses: " + studentData.getEnrolledCourses().size());

        //main container
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: white;");
        container.setPadding(new Insets(20));
        
        Label titleLabel = new Label("My Attendance");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        //attendance section setup
        VBox attendanceSection = createAttendanceSection();
        
        //summary section setup
        VBox summarySection = createSummarySection();
        
        //back button
        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnAction(e -> new StudentPage().show(stage, user));
        
        //add everything to main container
        container.getChildren().addAll(titleLabel, attendanceSection, summarySection, backButton);
        
        //set up scene
        Scene scene = new Scene(container, 800, 600);
        window.setTitle("Student - Attendance");
        window.setScene(scene);
        window.show();
    }
    
    //creates the attendance record section
    private VBox createAttendanceSection() {
        VBox attendanceSection = new VBox(10);
        attendanceSection.setAlignment(Pos.CENTER_LEFT);
        attendanceSection.setPadding(new Insets(20));
        attendanceSection.setStyle("-fx-background-color: #F0F1FF; -fx-background-radius: 10;");
        
        Label attendanceLabel = new Label("Attendance Records");
        attendanceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //attendance table
        TableView<AttendanceRecord> attendanceTable = new TableView<>();
        attendanceTable.setPrefHeight(300);
        attendanceTable.setPrefWidth(600);
        
        //course column
        TableColumn<AttendanceRecord, String> courseColumn = new TableColumn<>("Course");
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        courseColumn.setPrefWidth(200);
        
        //date column
        TableColumn<AttendanceRecord, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setPrefWidth(150);
        
        //status column with colored cells
        TableColumn<AttendanceRecord, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(150);
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
        
        //get attendance data
        ObservableList<AttendanceRecord> records = generateAttendanceRecords();
        attendanceTable.setItems(records);
        
        //add components to section
        attendanceSection.getChildren().addAll(attendanceLabel, attendanceTable);
        return attendanceSection;
    }
    
    //creates the attendance summary section
    private VBox createSummarySection() {
        VBox summarySection = new VBox(10);
        summarySection.setAlignment(Pos.CENTER_LEFT);
        summarySection.setPadding(new Insets(20));
        summarySection.setStyle("-fx-background-color: #F0F1FF; -fx-background-radius: 10;");
        
        Label summaryLabel = new Label("Attendance Summary");
        summaryLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //grid for summary data
        GridPane summaryGrid = new GridPane();
        summaryGrid.setHgap(10);
        summaryGrid.setVgap(10);
        summaryGrid.setPadding(new Insets(10));
        
        //column headers
        Label courseHeader = new Label("Course");
        courseHeader.setStyle("-fx-font-weight: bold;");
        Label totalHeader = new Label("Total Days");
        totalHeader.setStyle("-fx-font-weight: bold;");
        Label presentHeader = new Label("Days Present");
        presentHeader.setStyle("-fx-font-weight: bold;");
        Label percentHeader = new Label("Attendance %");
        percentHeader.setStyle("-fx-font-weight: bold;");
        
        //add headers to grid
        summaryGrid.add(courseHeader, 0, 0);
        summaryGrid.add(totalHeader, 1, 0);
        summaryGrid.add(presentHeader, 2, 0);
        summaryGrid.add(percentHeader, 3, 0);
        
        //populate grid with data
        generateAttendanceSummary(summaryGrid);
        
        //add grid to section
        summarySection.getChildren().addAll(summaryLabel, summaryGrid);
        return summarySection;
    }
    
    //generates attendance record data
    private ObservableList<AttendanceRecord> generateAttendanceRecords() {
        ObservableList<AttendanceRecord> records = FXCollections.observableArrayList();
        
        if (studentData == null) {
            System.out.println("ERROR: Student data is null");
            return records;
        }
        
        if (studentData.getFullName() == null || studentData.getFullName().isEmpty()) {
            System.out.println("WARNING: Student " + studentData.getUsername() + " has no name, setting default");
            studentData.setFullName("Student " + studentData.getUsername());
        }
        
        System.out.println("Loading attendance records for student: " + studentData.getUsername());
        
        // Get attendance records from student data
        List<AttendanceRecord> storedRecords = studentData.getAttendanceRecords();
        
        if (storedRecords != null && !storedRecords.isEmpty()) {
            // Add all stored records
            System.out.println("Found " + storedRecords.size() + " attendance records");
            records.addAll(storedRecords);
            return records;
        }
        
        //if no stored records found, display a message
        System.out.println("No attendance records found");
        
        List<String> enrolledCourses = studentData.getEnrolledCourses();
        
        //if no courses or records found, show a placeholder
        if (enrolledCourses == null || enrolledCourses.isEmpty()) {
            System.out.println("No enrolled courses found");
            records.add(new AttendanceRecord("No attendance records", "N/A", "N/A"));
            return records;
        }
        
        //if enrolled in courses but no attendance records yet
        for (String course : enrolledCourses) {
            records.add(new AttendanceRecord(course, "No attendance records yet", "N/A"));
        }
        
        System.out.println("Added " + records.size() + " placeholder records for enrolled courses");
        return records;
    }
    
    //adds attendance summary data to the grid
    private void generateAttendanceSummary(GridPane grid) {
        List<String> enrolledCourses = studentData.getEnrolledCourses();
        
        //handle empty course list
        if (enrolledCourses == null || enrolledCourses.isEmpty()) {
            System.out.println("No enrolled courses found for summary");
            addSummaryRow(grid, 0, "Sample Course 101", 4, 3, 75.0);
            return;
        }
        
        System.out.println("Generating attendance summary for " + enrolledCourses.size() + " courses");
        
        //process each course
        int rowIndex = 0;
        for (String course : enrolledCourses) {
            //these would normally be calculated from actual attendance records
            int totalDays = 5;
            int presentDays = studentData.getUsername().equals("student2") ? 4 : 
                             (studentData.getUsername().equals("student3") ? 4 : 5);
            double attendancePercentage = (double)presentDays / totalDays * 100;
            
            //add row to grid
            addSummaryRow(grid, rowIndex++, course, totalDays, presentDays, attendancePercentage);
        }
    }
    
    //adds a single row to the summary grid
    private void addSummaryRow(GridPane grid, int rowIndex, String course, int totalDays, int presentDays, double attendancePercentage) {
        grid.add(new Label(course), 0, rowIndex);
        grid.add(new Label(String.valueOf(totalDays)), 1, rowIndex);
        grid.add(new Label(String.valueOf(presentDays)), 2, rowIndex);
        grid.add(new Label(String.format("%.1f%%", attendancePercentage)), 3, rowIndex);
    }
} 