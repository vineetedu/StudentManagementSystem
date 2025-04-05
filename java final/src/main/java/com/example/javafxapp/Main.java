package com.example.javafxapp;

import javafx.application.Application;
import javafx.stage.Stage;

//main class that starts our application
public class Main extends Application {
    
    //starts the application and shows login page
    @Override
    public void start(Stage primaryStage) {
        //create and show the login page
        LoginPage loginPage = new LoginPage();
        loginPage.show(primaryStage);
    }
    
    public static void main(String[] args) {
        //launch the javafx application
        launch(args);
    }
} 