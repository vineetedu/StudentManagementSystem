package com.example.javafxapp.student;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class StudentProfilePage {
    public void show(Stage primaryStage, String username) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: white;");
        
        Label titleLabel = new Label("Profile Page");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
        backButton.setOnAction(e -> new StudentPage().show(primaryStage, username));
        
        container.getChildren().addAll(titleLabel, backButton);
        
        Scene scene = new Scene(container, 800, 600);
        primaryStage.setTitle("Student Portal - Profile");
        primaryStage.setScene(scene);
    }
} 