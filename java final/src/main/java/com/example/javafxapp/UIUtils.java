package com.example.javafxapp;

import javafx.scene.control.Button;

public class UIUtils {
    public static void styleButton(Button button) {
        button.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30; -fx-border-radius: 6px; -fx-background-radius: 6px;");
        
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #6790F5; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30; -fx-border-radius: 6px; -fx-background-radius: 6px;");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30; -fx-border-radius: 6px; -fx-background-radius: 6px;");
        });
    }
} 