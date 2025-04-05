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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import com.example.javafxapp.service.TeacherDataService;
import com.example.javafxapp.service.StudentDataService;
import com.example.javafxapp.model.TeacherData;
import com.example.javafxapp.model.StudentData;
import com.example.javafxapp.model.Assignment;
import java.util.List;

public class TeacherInputGradesPage {
    //window and user data
    private Stage window;
    private String currentUser;
    
    //data services
    private TeacherDataService teacherDataService;
    private StudentDataService studentDataService;
    
    //teacher data
    private TeacherData teacherData;

    //add class fields to store the combo box references
    private ComboBox<String> coursesComboBox;
    private ComboBox<String> studentsComboBox;
    
    //getter methods for the combo boxes
    private ComboBox<String> getCourseComboBox() {
        return coursesComboBox;
    }
    
    private ComboBox<String> getStudentComboBox() {
        return studentsComboBox;
    }

    public void show(Stage stage, String user) {
        //store the window and user
        this.window = stage;
        this.currentUser = user;
        
        //get the service instances
        this.teacherDataService = TeacherDataService.getInstance();
        this.studentDataService = StudentDataService.getInstance();
        
        //get the teacher data - always use "teacher" as the name
        this.teacherData = teacherDataService.getTeacherData("teacher");
        
        //check if teacher data exists
        if (teacherData == null) {
            //create an error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data Error");
            alert.setContentText("Could not load teacher data. Please contact support.");
            
            //show the alert
            alert.showAndWait();
            
            //exit without showing the page
            return;
        }

        //create the main container
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: white;");
        container.setPadding(new Insets(20));
        
        //create the page title
        Label titleLabel = new Label("Input Grades");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        //create the selection section for course and student
        HBox selectionSection = new HBox(20);
        selectionSection.setAlignment(Pos.CENTER);
        selectionSection.setPadding(new Insets(20));
        
        //create the course selection box
        VBox courseSelectionBox = createCourseSelectionBox();
        
        //create the student selection box
        VBox studentSelectionBox = createStudentSelectionBox();
        
        //add both selection boxes to the selection section
        selectionSection.getChildren().addAll(courseSelectionBox, studentSelectionBox);
        
        //extract the combo boxes for use in event handlers
        ComboBox<String> coursesComboBox = getCourseComboBox();
        ComboBox<String> studentsComboBox = getStudentComboBox();
        
        //create the assignments section
        VBox assignmentsSection = new VBox(10);
        assignmentsSection.setAlignment(Pos.CENTER_LEFT);
        assignmentsSection.setPadding(new Insets(20));
        assignmentsSection.setStyle("-fx-background-color: #F0F1FF; -fx-background-radius: 10;");
        
        //create the assignments section title
        Label assignmentsLabel = new Label("Student Assignments");
        assignmentsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //create the assignments table
        TableView<AssignmentEntry> assignmentsTable = new TableView<>();
        assignmentsTable.setPrefHeight(300);
        assignmentsTable.setPrefWidth(600);
        
        //set up the table columns
        setupTableColumns(assignmentsTable, coursesComboBox, studentsComboBox);
        
        //add the label and table to the assignments section
        assignmentsSection.getChildren().addAll(assignmentsLabel, assignmentsTable);
        
        //set up course selection event handler
        setupCourseSelectionHandler(coursesComboBox, studentsComboBox, assignmentsTable);
        
        //set up student selection event handler
        setupStudentSelectionHandler(coursesComboBox, studentsComboBox, assignmentsTable);
        
        //create the back button
        Button backButton = createBackButton(stage, user);
        
        //add all components to the main container
        container.getChildren().addAll(titleLabel, selectionSection, assignmentsSection, backButton);
        
        //create and set the scene
        Scene scene = new Scene(container, 800, 600);
        window.setTitle("Teacher - Input Grades");
        window.setScene(scene);
        
        //show the window
        window.show();
    }
    
    //create the course selection box
    private VBox createCourseSelectionBox() {
        //create container
        VBox courseSelectionBox = new VBox(10);
        courseSelectionBox.setAlignment(Pos.CENTER_LEFT);
        
        //create label
        Label coursesLabel = new Label("Select Course:");
        coursesLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        //create dropdown
        ComboBox<String> coursesComboBox = new ComboBox<>();
        coursesComboBox.setPrefWidth(200);
        
        //store the combo box for later reference
        this.coursesComboBox = coursesComboBox;
        
        //get the list of courses from teacher data
        List<String> courses = teacherData.getAssignedCourses();
        
        //add courses to the dropdown
        coursesComboBox.getItems().addAll(courses);
        
        //add components to container
        courseSelectionBox.getChildren().addAll(coursesLabel, coursesComboBox);
        
        //return the completed container
        return courseSelectionBox;
    }
    
    //create the student selection box
    private VBox createStudentSelectionBox() {
        //create container
        VBox studentSelectionBox = new VBox(10);
        studentSelectionBox.setAlignment(Pos.CENTER_LEFT);
        
        //create label
        Label studentsLabel = new Label("Select Student:");
        studentsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        //create dropdown (disabled until a course is selected)
        ComboBox<String> studentsComboBox = new ComboBox<>();
        studentsComboBox.setPrefWidth(200);
        studentsComboBox.setDisable(true);
        
        //store the combo box for later reference
        this.studentsComboBox = studentsComboBox;
        
        //add components to container
        studentSelectionBox.getChildren().addAll(studentsLabel, studentsComboBox);
        
        //return the completed container
        return studentSelectionBox;
    }
    
    //set up the table columns
    private void setupTableColumns(TableView<AssignmentEntry> table, 
                                  ComboBox<String> coursesComboBox, 
                                  ComboBox<String> studentsComboBox) {
        //title column
        TableColumn<AssignmentEntry, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(200);
        
        //due date column
        TableColumn<AssignmentEntry, String> dueDateColumn = new TableColumn<>("Due Date");
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        dueDateColumn.setPrefWidth(100);
        
        //status column
        TableColumn<AssignmentEntry, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(100);
        
        //grade column with custom cell factory
        TableColumn<AssignmentEntry, Double> gradeColumn = createGradeColumn();
        
        //action column with save button
        TableColumn<AssignmentEntry, String> actionColumn = createActionColumn(table, coursesComboBox, studentsComboBox);
        
        //add all columns to the table
        table.getColumns().addAll(titleColumn, dueDateColumn, statusColumn, gradeColumn, actionColumn);
        
        //make the table editable
        table.setEditable(true);
    }
    
    //create the grade column
    private TableColumn<AssignmentEntry, Double> createGradeColumn() {
        TableColumn<AssignmentEntry, Double> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeColumn.setPrefWidth(100);
        
        gradeColumn.setCellFactory(column -> new TableCell<AssignmentEntry, Double>() {
            private final TextField textField = new TextField();
            
            {
                textField.textProperty().addListener((obs, oldValue, newValue) -> {
                    if (newValue != null && !newValue.isEmpty()) {
                        try {
                            Double value = Double.parseDouble(newValue);
                            AssignmentEntry entry = getTableView().getItems().get(getIndex());
                            if (entry != null) {
                                entry.setGrade(value);
                            }
                        } catch (NumberFormatException e) {
                            //not a valid number
                        }
                    }
                });
            }
            
            @Override
            protected void updateItem(Double grade, boolean empty) {
                super.updateItem(grade, empty);
                
                if (empty) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                
                AssignmentEntry entry = getTableView().getItems().get(getIndex());
                if (entry == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                
                if (entry.getStatus().equals("Graded") && grade != null) {
                    setText(String.format("%.1f", grade));
                    setGraphic(null);
                } else if (entry.getStatus().equals("Submitted")) {
                    textField.setText(grade != null ? String.format("%.1f", grade) : "");
                    setGraphic(textField);
                    setText(null);
                } else {
                    setText("N/A");
                    setGraphic(null);
                }
            }
        });
        
        return gradeColumn;
    }
    
    //create the action column with save button
    private TableColumn<AssignmentEntry, String> createActionColumn(TableView<AssignmentEntry> table, 
                                                                  ComboBox<String> coursesComboBox, 
                                                                  ComboBox<String> studentsComboBox) {
        TableColumn<AssignmentEntry, String> actionColumn = new TableColumn<>("Action");
        actionColumn.setPrefWidth(100);
        
        actionColumn.setCellFactory(column -> new TableCell<AssignmentEntry, String>() {
            private final Button saveButton = new Button("Save");
            
            {
                saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty) {
                    setGraphic(null);
                    return;
                }
                
                AssignmentEntry currentEntry = getTableView().getItems().get(getIndex());
                if (currentEntry.getStatus().equals("Submitted")) {
                    setGraphic(saveButton);
                } else {
                    setGraphic(null);
                }
                
                saveButton.setOnAction(e -> {
                    String selectedCourse = coursesComboBox.getValue();
                    String selectedDisplayName = studentsComboBox.getValue();
                    
                    if (selectedCourse != null && selectedDisplayName != null) {
                        AssignmentEntry assignmentEntry = getTableView().getItems().get(getIndex());
                        String selectedStudent = selectedDisplayName.split(" \\(")[0];
                        
                        if (assignmentEntry.getStatus().equals("Submitted")) {
                            try {
                                Double gradeValue = assignmentEntry.getGrade();
                                
                                if (gradeValue != null) {
                                    if (gradeValue >= 0 && gradeValue <= 100) {
                                        String assignmentTitle = assignmentEntry.getTitle();
                                        teacherDataService.updateStudentGrade(currentUser, selectedCourse, selectedStudent, assignmentTitle, gradeValue);
                                        
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Grade Saved");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Grade has been saved successfully!");
                                        alert.showAndWait();
                                        
                                        loadStudentAssignments(selectedCourse, selectedStudent, table);
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Invalid Grade");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Grade must be between 0 and 100.");
                                        alert.showAndWait();
                                    }
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Invalid Grade");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Please enter a valid grade before saving.");
                                    alert.showAndWait();
                                }
                            } catch (NumberFormatException ex) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Invalid Grade");
                                alert.setHeaderText(null);
                                alert.setContentText("Please enter a valid number for the grade.");
                                alert.showAndWait();
                            }
                        }
                    }
                });
            }
        });
        
        return actionColumn;
    }
    
    //set up course selection event handler
    private void setupCourseSelectionHandler(ComboBox<String> coursesComboBox,
                                            ComboBox<String> studentsComboBox,
                                            TableView<AssignmentEntry> assignmentsTable) {
        coursesComboBox.setOnAction(e -> {
            String selectedCourse = coursesComboBox.getValue();
            
            if (selectedCourse != null) {
                List<String> enrolledStudents = teacherData.getStudentsInCourse(selectedCourse);
                
                studentsComboBox.getItems().clear();
                
                for (String studentUsername : enrolledStudents) {
                    StudentData studentData = studentDataService.getStudentData(studentUsername);
                    
                    if (studentData != null) {
                        String displayName = studentUsername + " (" + studentData.getFullName() + ")";
                        studentsComboBox.getItems().add(displayName);
                    }
                }
                
                studentsComboBox.setDisable(enrolledStudents.isEmpty());
                assignmentsTable.getItems().clear();
            }
        });
    }
    
    //set up student selection event handler
    private void setupStudentSelectionHandler(ComboBox<String> coursesComboBox,
                                             ComboBox<String> studentsComboBox,
                                             TableView<AssignmentEntry> assignmentsTable) {
        studentsComboBox.setOnAction(e -> {
            String selectedCourse = coursesComboBox.getValue();
            String selectedDisplayName = studentsComboBox.getValue();
            
            if (selectedCourse != null && selectedDisplayName != null) {
                String selectedStudent = selectedDisplayName.split(" \\(")[0];
                loadStudentAssignments(selectedCourse, selectedStudent, assignmentsTable);
            }
        });
    }
    
    //create the back button
    private Button createBackButton(Stage stage, String user) {
        //create the button
        Button backButton = new Button("Back to Dashboard");
        
        //style the button
        backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        
        //add hover effects
        backButton.setOnMouseEntered(e -> 
            backButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnMouseExited(e -> 
            backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        
        //set action to return to dashboard
        backButton.setOnAction(e -> new TeacherPage().show(stage, user));
        
        //return the completed button
        return backButton;
    }
    
    //load assignments for a student in a course
    private void loadStudentAssignments(String course, String studentUsername, TableView<AssignmentEntry> table) {
        ObservableList<AssignmentEntry> assignments = FXCollections.observableArrayList();
        
        StudentData studentData = studentDataService.getStudentData(studentUsername);
        if (studentData != null) {
            List<Assignment> allAssignments = studentData.getAssignments();
            
            if (allAssignments != null) {
                boolean foundAny = false;
                
                for (Assignment assignment : allAssignments) {
                    if (assignment.getCourse().equals(course)) {
                        foundAny = true;
                        
                        String status;
                        if (assignment.isSubmitted()) {
                            status = assignment.isGraded() ? "Graded" : "Submitted";
                        } else {
                            status = "Not Submitted";
                        }
                        
                        Double grade = assignment.isGraded() ? assignment.getScore() : null;
                        
                        AssignmentEntry entry = new AssignmentEntry(
                            assignment.getTitle(),
                            assignment.getDueDate(),
                            status,
                            grade
                        );
                        
                        assignments.add(entry);
                    }
                }
                
                if (!foundAny) {
                    assignments.add(new AssignmentEntry("No assignments found", "", "N/A", null));
                }
            } else {
                assignments.add(new AssignmentEntry("No assignments found", "", "N/A", null));
            }
        } else {
            assignments.add(new AssignmentEntry("Error loading assignments", "", "N/A", null));
        }
        
        table.setItems(assignments);
    }

    //helper class to represent an assignment in the table
    public static class AssignmentEntry {
        //entry properties
        private final String title;
        private final String dueDate;
        private final String status;
        private Double grade;
        
        //constructor
        public AssignmentEntry(String title, String dueDate, String status, Double grade) {
            //store values
            this.title = title;
            this.dueDate = dueDate;
            this.status = status;
            this.grade = grade;
        }
        
        //getters and setters
        public String getTitle() { return title; }
        public String getDueDate() { return dueDate; }
        public String getStatus() { return status; }
        public Double getGrade() { return grade; }
        public void setGrade(Double grade) { this.grade = grade; }
    }
} 