package com.example.javafxapp.teacher;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class TeacherInputGradesPage {
    private Stage window;
    private String currentUser;

    public void show(Stage stage, String user) {
        this.window = stage;
        this.currentUser = user;

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: white;");
        
        Label titleLabel = new Label("Input Student Grades");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Button backButton = new Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6;");
        backButton.setOnAction(e -> new TeacherPage().show(stage, user));
        
        container.getChildren().addAll(titleLabel, backButton);
        
        Scene scene = new Scene(container, 800, 600);
        window.setTitle("Teacher - Input Grades");
        window.setScene(scene);
        window.show();
    }
} 