package com.example.javafxapp.admin;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import com.example.javafxapp.UIUtils;

//page for adding and deleting user accounts
public class AdminAddDeletePage {
    private Stage mainWindow;
    private String adminUsername;

    //show the add/delete account page
    public void show(Stage stage, String user) {
        this.mainWindow = stage;
        this.adminUsername = user;

        //setup main container
        VBox pageContainer = new VBox(20);
        pageContainer.setAlignment(Pos.CENTER);
        pageContainer.setPadding(new Insets(20));
        pageContainer.setStyle("-fx-background-color: white;");
        
        //add page title
        Label pageTitle = new Label("Add/Delete Account");
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        //create back button
        Button backButton = new Button("Back to Dashboard");
        UIUtils.styleButton(backButton);
        backButton.setOnAction(e -> goBackToDashboard(stage));
        
        //add everything to the container
        pageContainer.getChildren().addAll(pageTitle, backButton);
        
        //show the page
        Scene pageScene = new Scene(pageContainer, 800, 600);
        mainWindow.setTitle("Admin - Add/Delete Account");
        mainWindow.setScene(pageScene);
        mainWindow.show();
    }
    
    //return to the admin dashboard
    private void goBackToDashboard(Stage stage) {
        new AdminPage().show(stage, adminUsername);
    }
} 