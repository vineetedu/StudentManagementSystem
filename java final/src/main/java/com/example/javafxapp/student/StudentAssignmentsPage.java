package com.example.javafxapp.student;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import com.example.javafxapp.service.StudentDataService;
import com.example.javafxapp.model.StudentData;
import com.example.javafxapp.model.Assignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StudentAssignmentsPage {
    private Stage window;
    private String currentUser;
    private StudentDataService dataService;
    private StudentData studentData;

    //student assignments page
    public void show(Stage stage, String user) {
        //setting up the stage and current user
        this.window = stage;
        this.currentUser = user;
        this.dataService = StudentDataService.getInstance();
        this.studentData = dataService.getStudentData(user);

        //setting up the container
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: white;");
        container.setPadding(new Insets(20));
        
        //title label
        Label titleLabel = new Label("Assignments");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        //vbox for assignments
        VBox assignmentsSection = new VBox(10);
        assignmentsSection.setAlignment(Pos.CENTER_LEFT);
        assignmentsSection.setPadding(new Insets(20));
        assignmentsSection.setStyle("-fx-background-color: #F0F1FF; -fx-background-radius: 10;");
        
        //label for assignments
        Label assignmentsLabel = new Label("Current Assignments");
        assignmentsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        //list to show all assignments assigned to the student
        ListView<Assignment> assignmentsList = new ListView<>();
        assignmentsList.setPrefHeight(300);
        assignmentsList.setPrefWidth(400);
        
        //list cell to display assignment details
        assignmentsList.setCellFactory(lv -> new ListCell<Assignment>() {
            @Override
            protected void updateItem(Assignment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { //if empty or null, set text to null
                    setText(null);
                } else {
                    setText(String.format("%s - %s\nDue: %s\nStatus: %s",//formatting the text to display the assignment details
                        item.getCourse(),
                        item.getTitle(),
                        item.getDueDate(),
                        item.isSubmitted() ? "Submitted" : "Pending" //if submitted, set text to submitted, otherwise set text to pending
                    ));
                }
            }
        });
        
        //loading assignments for enrolled courses
        ObservableList<Assignment> assignments = FXCollections.observableArrayList(studentData.getAssignments());
        assignmentsList.setItems(assignments);
        
        //submit button to submit an assignment
        Button submitButton = new Button("Submit Assignment");
        submitButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        submitButton.setOnMouseEntered(e -> submitButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        submitButton.setOnAction(e -> {
            Assignment selectedAssignment = assignmentsList.getSelectionModel().getSelectedItem();
            if (selectedAssignment != null && !selectedAssignment.isSubmitted()) { //if the assignment is not submitted, set it to submitted
                selectedAssignment.setSubmitted(true);
                assignmentsList.refresh();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION); //alert for successful submission
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Assignment submitted successfully!");
                alert.showAndWait();
            } else if (selectedAssignment != null && selectedAssignment.isSubmitted()) { //if the assignment is already submitted, show a warning
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("This assignment has already been submitted.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING); //warning for no assignment selected
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select an assignment to submit.");
                alert.showAndWait();
            }
        });
        
        assignmentsSection.getChildren().addAll(assignmentsLabel, assignmentsList, submitButton);
        
        //back button to go back to the dashboard
        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnAction(e -> new StudentPage().show(stage, user));
        
        container.getChildren().addAll(titleLabel, assignmentsSection, backButton);
        
        //setting up the scene
        Scene scene = new Scene(container, 800, 600);
        window.setTitle("Student - Assignments");
        window.setScene(scene);
        window.show();
    }
} 