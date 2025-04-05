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
import com.example.javafxapp.model.CourseSummary;
import java.util.List;
import java.util.ArrayList;

/**
 * displays a student's grades and course performance.
 */
public class StudentGradesPage {
    private Stage mainWindow;
    private String studentUsername;
    private StudentDataService dataService;
    private StudentData studentData;

    public void show(Stage stage, String user) {
        this.mainWindow = stage;
        this.studentUsername = user;
        this.dataService = StudentDataService.getInstance();
        this.studentData = dataService.getStudentData(user);
        
        //validate student data
        if (studentData == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data Error");
            alert.setContentText("Could not load student data. Please contact support.");
            alert.showAndWait();
            return;
        }

        //ensure student has a name
        if (studentData.getFullName() == null || studentData.getFullName().isEmpty()) {
            studentData.setFullName("Student " + studentData.getUsername());
        }

        //setup main container
        VBox pageContainer = new VBox(20);
        pageContainer.setAlignment(Pos.CENTER);
        pageContainer.setStyle("-fx-background-color: white;");
        pageContainer.setPadding(new Insets(20));
        
        //add page title
        Label pageTitle = new Label("My Grades");
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        //create sections
        VBox assignmentsSection = createAssignmentsSection();
        VBox summarySection = createSummarySection();
        
        //create back button
        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnAction(e -> new StudentPage().show(stage, user));
        
        //combine all elements
        pageContainer.getChildren().addAll(pageTitle, assignmentsSection, summarySection, backButton);
        
        //show the page
        Scene pageScene = new Scene(pageContainer, 800, 600);
        mainWindow.setTitle("Student - My Grades");
        mainWindow.setScene(pageScene);
        mainWindow.show();
    }

    /**
     * creates the assignments display section.
     */
    private VBox createAssignmentsSection() {
        VBox assignmentsSection = new VBox(10);
        assignmentsSection.setAlignment(Pos.CENTER_LEFT);
        assignmentsSection.setPadding(new Insets(20));
        assignmentsSection.setStyle("-fx-background-color: #F0F1FF; -fx-background-radius: 10;");
        
        //add section header
        Label assignmentsLabel = new Label("My Assignments");
        assignmentsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //setup assignments table
        TableView<Assignment> assignmentsTable = new TableView<>();
        assignmentsTable.setPrefHeight(200);
        assignmentsTable.setPrefWidth(600);
        
        //setup table columns
        TableColumn<Assignment, String> courseColumn = new TableColumn<>("Course");
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        courseColumn.setPrefWidth(150);
        
        TableColumn<Assignment, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(150);
        
        TableColumn<Assignment, String> dueDateColumn = new TableColumn<>("Due Date");
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        dueDateColumn.setPrefWidth(100);
        
        //submission status column with color coding
        TableColumn<Assignment, Boolean> submittedColumn = new TableColumn<>("Submitted");
        submittedColumn.setCellValueFactory(new PropertyValueFactory<>("submitted"));
        submittedColumn.setPrefWidth(100);
        submittedColumn.setCellFactory(column -> new TableCell<Assignment, Boolean>() {
            @Override
            protected void updateItem(Boolean submitted, boolean empty) {
                super.updateItem(submitted, empty);
                if (empty || submitted == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(submitted ? "Yes" : "No");
                    setStyle(submitted ? "-fx-text-fill: green; -fx-font-weight: bold;" : "-fx-text-fill: red;");
                }
            }
        });
        
        //score column with formatting
        TableColumn<Assignment, Double> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setPrefWidth(100);
        scoreColumn.setCellFactory(column -> new TableCell<Assignment, Double>() {
            @Override
            protected void updateItem(Double score, boolean empty) {
                super.updateItem(score, empty);
                if (empty || score == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Assignment assignment = getTableView().getItems().get(getIndex());
                    if (assignment != null && assignment.isGraded()) {
                        setText(String.format("%.1f", score));
                        setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
                    } else {
                        setText("Not graded");
                        setStyle("-fx-text-fill: gray;");
                    }
                }
            }
        });
        
        //add columns to table
        assignmentsTable.getColumns().addAll(courseColumn, titleColumn, dueDateColumn, submittedColumn, scoreColumn);
        
        //load assignments data
        List<Assignment> assignments = studentData.getAssignments();
        if (assignments == null) {
            assignments = new ArrayList<>();
        }
        
        //add sample data if no assignments exist
        if (assignments.isEmpty()) {
            Assignment sampleAssignment = new Assignment("Sample Course", "Sample Assignment", 
                                                        "This is a sample assignment", "05/01/2024");
            assignments.add(sampleAssignment);
        }
        
        //set table data
        ObservableList<Assignment> assignmentList = FXCollections.observableArrayList(assignments);
        assignmentsTable.setItems(assignmentList);
        
        //add components to section
        assignmentsSection.getChildren().addAll(assignmentsLabel, assignmentsTable);
        return assignmentsSection;
    }

    /**
     * creates the course summary section.
     */
    private VBox createSummarySection() {
        VBox summarySection = new VBox(10);
        summarySection.setAlignment(Pos.CENTER_LEFT);
        summarySection.setPadding(new Insets(20));
        summarySection.setStyle("-fx-background-color: #FFF0F1; -fx-background-radius: 10;");
        
        //add section header
        Label summaryLabel = new Label("Course Summaries");
        summaryLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //setup summary table
        TableView<CourseSummary> summaryTable = new TableView<>();
        summaryTable.setPrefHeight(150);
        summaryTable.setPrefWidth(600);
        
        //setup table columns
        TableColumn<CourseSummary, String> courseSummaryColumn = new TableColumn<>("Course");
        courseSummaryColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseSummaryColumn.setPrefWidth(300);
        
        TableColumn<CourseSummary, String> completedAssignmentsColumn = new TableColumn<>("Assignments Completed");
        completedAssignmentsColumn.setCellValueFactory(new PropertyValueFactory<>("completedAssignments"));
        completedAssignmentsColumn.setPrefWidth(300);
        
        //add columns to table
        summaryTable.getColumns().addAll(courseSummaryColumn, completedAssignmentsColumn);
        
        //load and display summaries
        ObservableList<CourseSummary> courseSummaries = generateCourseSummaries();
        summaryTable.setItems(courseSummaries);
        
        //add components to section
        summarySection.getChildren().addAll(summaryLabel, summaryTable);
        return summarySection;
    }

    /**
     * generates summaries for each enrolled course.
     */
    private ObservableList<CourseSummary> generateCourseSummaries() {
        ObservableList<CourseSummary> summaries = FXCollections.observableArrayList();
        
        if (studentData == null) {
            summaries.add(new CourseSummary("Error", "0/0"));
            return summaries;
        }
        
        List<String> enrolledCourses = studentData.getEnrolledCourses();
        
        if (enrolledCourses == null || enrolledCourses.isEmpty()) {
            summaries.add(new CourseSummary("No courses enrolled", "0/0"));
            return summaries;
        }
        
        List<Assignment> assignments = studentData.getAssignments();
        if (assignments == null) {
            assignments = new ArrayList<>();
        }
        
        //process each course
        for (String course : enrolledCourses) {
            int totalAssignments = 0;
            int submittedAssignments = 0;
            
            //calculate course metrics
            for (Assignment assignment : assignments) {
                if (assignment.getCourse().equals(course)) {
                    totalAssignments++;
                    if (assignment.isSubmitted()) {
                        submittedAssignments++;
                    }
                }
            }
            
            //create summary
            summaries.add(new CourseSummary(
                course,
                submittedAssignments + "/" + totalAssignments
            ));
        }
        
        return summaries;
    }
} 