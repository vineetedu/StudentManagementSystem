package com.example.javafxapp;

import javafx.scene.control.Button;

//utility class for common ui operations
public class UIUtils {
    
    //applies our standard button style with hover effects
    public static void styleButton(Button button) {
        //default button style
        String defaultStyle = "-fx-background-color: #5664F5; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-padding: 10 30; " +
                            "-fx-border-radius: 6px; " +
                            "-fx-background-radius: 6px;";
        
        //hover style with lighter color
        String hoverStyle = "-fx-background-color: #6790F5; " +
                          "-fx-text-fill: white; " +
                          "-fx-font-size: 14px; " +
                          "-fx-padding: 10 30; " +
                          "-fx-border-radius: 6px; " +
                          "-fx-background-radius: 6px;";
        
        //set initial style
        button.setStyle(defaultStyle);
        
        //add hover effect
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        
        //restore default style when mouse exits
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));
    }
} 