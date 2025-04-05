package com.example.javafxapp.admin;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.javafxapp.service.StudentDataService;
import com.example.javafxapp.model.StudentData;
import com.example.javafxapp.UIUtils;
import java.util.Map;

/**
 * this is where the admin can update student information
 */
public class AdminUpdateAccountPage {
    //main window of the application
    private Stage window;
    
    //keep track of who's logged in
    private String currentUser;
    
    //we need this to manage student data
    private StudentDataService studentDataService;
    
    //ui components we need to access from different methods
    private ComboBox<String> studentSelector;
    private TextField fullNameField;
    private TextField emailField;
    private ListView<String> enrolledCoursesList;
    
    //keep track of which student we're updating
    private StudentData selectedStudent;

    //this shows the account update page
    public void show(Stage stage, String user) {
        //store the window and user info for later
        this.window = stage;
        this.currentUser = user;
        
        //get data service ready
        this.studentDataService = StudentDataService.getInstance();

        //make the main container look nice
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: white;"); //white background
        container.setPadding(new Insets(20));

        //add a title
        Label titleLabel = new Label("Update Student Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        //create the student selection dropdown
        HBox selectionBox = createStudentSelector();

        //create the form for updating student info
        GridPane formGrid = createUpdateForm();

        //create the courses section
        VBox coursesBox = createCoursesSection();

        //create update button to save changes
        Button updateButton = new Button("Update Account");
        UIUtils.styleButton(updateButton);
        updateButton.setOnAction(e -> updateStudentData());

        //add a back button to return to dashboard
        Button backButton = new Button("Back to Dashboard");
        UIUtils.styleButton(backButton);
        backButton.setOnAction(e -> goBackToDashboard(stage, user));

        //put it all together
        container.getChildren().addAll(
            titleLabel,
            selectionBox,
            formGrid,
            coursesBox,
            updateButton,
            backButton
        );

        //show pae
        Scene scene = new Scene(container, 800, 600);
        window.setTitle("Admin - Update Account");
        window.setScene(scene);
        window.show();
    }

    //creates the dropdown 
    private HBox createStudentSelector() {
        HBox selectionBox = new HBox(10);
        selectionBox.setAlignment(Pos.CENTER);
        
        //add a label
        Label studentLabel = new Label("Select Student:");
        
        //create the dropdown
        studentSelector = new ComboBox<>();
        studentSelector.setPrefWidth(200);
        
        //load available students
        loadStudents();
        
        //update form when a student is selected
        studentSelector.setOnAction(e -> loadStudentData());
        
        //put it together
        selectionBox.getChildren().addAll(studentLabel, studentSelector);
        
        return selectionBox;
    }

    //creates the form for updating student information
    private GridPane createUpdateForm() {
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

        //full name field
        Label nameLabel = new Label("Full Name:");
        fullNameField = new TextField();
        fullNameField.setPrefWidth(200);
        formGrid.addRow(0, nameLabel, fullNameField);

        //email field
        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPrefWidth(200);
        formGrid.addRow(1, emailLabel, emailField);

        return formGrid;
    }

    //creates the section showing enrolled courses
    private VBox createCoursesSection() {
        //create label
        Label coursesLabel = new Label("Enrolled Courses:");
        
        //create list view
        enrolledCoursesList = new ListView<>();
        enrolledCoursesList.setPrefHeight(150);
        enrolledCoursesList.setPrefWidth(200);
        
        //put them together
        return new VBox(5, coursesLabel, enrolledCoursesList);
    }

    //loads the list of available students
    private void loadStudents() {
        //create a list for the dropdown
        ObservableList<String> studentUsernames = FXCollections.observableArrayList();
        
        //get all students from the service
        Map<String, StudentData> students = studentDataService.getAllStudents();
        
        //add their usernames to the list
        studentUsernames.addAll(students.keySet());
        
        //update the dropdown
        studentSelector.setItems(studentUsernames);
    }

    //loads the selected student's data into the form
    private void loadStudentData() {
        //get the selected username
        String selectedUsername = studentSelector.getValue();
        
        //make sure something is selected
        if (selectedUsername != null) {
            //get the student's data
            selectedStudent = studentDataService.getStudentData(selectedUsername);
            
            //update the form if we found the student
            if (selectedStudent != null) {
                fullNameField.setText(selectedStudent.getFullName());
                emailField.setText(selectedStudent.getEmail());
                enrolledCoursesList.setItems(FXCollections.observableArrayList(selectedStudent.getEnrolledCourses()));
            }
        }
    }

    //saves the updated student information
    private void updateStudentData() {
        //make sure we have a student selected
        if (selectedStudent != null) {
            //update their information
            selectedStudent.setFullName(fullNameField.getText());
            selectedStudent.setEmail(emailField.getText());
            
            //save the changes
            studentDataService.updateStudentData(selectedStudent);
            
            //show a success message
            showSuccessMessage("Student account updated successfully!");
        } else {
            //show an error if no student is selected
            showErrorMessage("Please select a student to update.");
        }
    }
    
    //shows a success message to the user
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    //shows an error message to the user
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    //go back to the admin dashboard
    private void goBackToDashboard(Stage stage, String user) {
        new AdminPage().show(stage, user);
    }
} 