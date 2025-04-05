package com.example.javafxapp.student;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import com.example.javafxapp.service.StudentDataService;
import com.example.javafxapp.model.StudentData;

public class StudentPersonalDetailsPage {
    private Stage window;
    private String currentUser;
    private StudentDataService dataService;
    private StudentData studentData;

    public void show(Stage stage, String user) {
        this.window = stage;
        this.currentUser = user;
        this.dataService = StudentDataService.getInstance();
        this.studentData = dataService.getStudentData(user);

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: white;");
        container.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Personal Details");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        //vbox for personal details
        VBox formSection = new VBox(15);
        formSection.setAlignment(Pos.CENTER_LEFT);
        formSection.setPadding(new Insets(20));
        formSection.setStyle("-fx-background-color: #F0F1FF; -fx-background-radius: 10;");
        
        //fields for personal details
        TextField nameField = new TextField(studentData.getFullName());
        nameField.setPromptText("Full Name");
        nameField.setPrefWidth(300);
        
        TextField emailField = new TextField(studentData.getEmail());
        emailField.setPromptText("Email Address");
        emailField.setPrefWidth(300);
        
        TextField phoneField = new TextField(studentData.getPhone());
        phoneField.setPromptText("Phone Number");
        phoneField.setPrefWidth(300);
        
        TextArea addressArea = new TextArea(studentData.getAddress());
        addressArea.setPromptText("Address");
        addressArea.setPrefWidth(300);
        addressArea.setPrefHeight(100);
        
        //save button
        Button saveButton = new Button("Save Changes");
        saveButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        saveButton.setOnMouseEntered(e -> saveButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        saveButton.setOnMouseExited(e -> saveButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        saveButton.setOnAction(e -> { //saving the changes
            studentData.setFullName(nameField.getText());
            studentData.setEmail(emailField.getText());
            studentData.setPhone(phoneField.getText());
            studentData.setAddress(addressArea.getText());
            dataService.updateStudentData(studentData);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION); //alert for successful update
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Your personal details have been updated successfully.");
            alert.showAndWait();
        });
        
        formSection.getChildren().addAll( //adding the components to the vbox
            new Label("Full Name:"), nameField,
            new Label("Email:"), emailField,
            new Label("Phone:"), phoneField,
            new Label("Address:"), addressArea,
            saveButton
        );
        
        //back button
        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;"));
        backButton.setOnAction(e -> new StudentPage().show(stage, user));
        
        container.getChildren().addAll(titleLabel, formSection, backButton);
        
        Scene scene = new Scene(container, 800, 600);
        window.setTitle("Student - Personal Details");
        window.setScene(scene);
        window.show();
    }
}