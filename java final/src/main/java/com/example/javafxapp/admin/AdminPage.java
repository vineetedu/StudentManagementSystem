package com.example.javafxapp.admin;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import com.example.javafxapp.LoginPage;

//main admin dashboard page
public class AdminPage {
    private Stage mainWindow;
    private String adminUsername;

    //display the admin dashboard
    public void show(Stage stage, String user) {
        this.mainWindow = stage;
        this.adminUsername = user;

        //setup the main layout
        HBox pageLayout = new HBox();
        pageLayout.setStyle("-fx-background-color: #f5f6fa;");

        //create the left sidebar menu
        VBox sideMenu = new VBox(15);
        sideMenu.setPrefWidth(200);
        sideMenu.setPadding(new Insets(20));
        sideMenu.setStyle("-fx-background-color: #F0F1FF; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        //add the page title
        Label pageTitle = new Label("Admin Portal");
        pageTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        //create menu buttons
        Button updateAccountButton = new Button("Update Account");
        Button addDeleteButton = new Button("Add/Delete Account");
        Button assignTeacherButton = new Button("Assign Teacher");
        Button generateReportButton = new Button("Generate Report");

        //style all menu buttons
        Button[] menuButtons = {updateAccountButton, addDeleteButton, assignTeacherButton, generateReportButton};
        String defaultButtonStyle = "-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-padding: 10 20; -fx-background-radius: 6;";
        String hoverButtonStyle = "-fx-background-color:rgb(219, 221, 247); -fx-text-fill: black; -fx-font-size: 12px; -fx-padding: 10 20; -fx-background-radius: 6;";
        
        for (Button menuButton : menuButtons) {
            menuButton.setPrefWidth(160);
            menuButton.setStyle(defaultButtonStyle);
            menuButton.setOnMouseEntered(e -> menuButton.setStyle(hoverButtonStyle));
            menuButton.setOnMouseExited(e -> menuButton.setStyle(defaultButtonStyle));
        }

        //setup button actions
        updateAccountButton.setOnAction(e -> openUpdateAccount());
        addDeleteButton.setOnAction(e -> openAddDelete());
        assignTeacherButton.setOnAction(e -> openAssignTeacher());
        generateReportButton.setOnAction(e -> openGenerateReport());

        //add all items to side menu
        sideMenu.getChildren().addAll(pageTitle, new Separator(), updateAccountButton, addDeleteButton, assignTeacherButton, generateReportButton);

        //create main content area
        VBox contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));
        contentArea.setStyle("-fx-background-color: white;");

        //create top bar with logout button
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        
        Button logoutButton = new Button("Log Out");
        String logoutDefaultStyle = "-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;";
        String logoutHoverStyle = "-fx-background-color: #6790F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;";
        logoutButton.setStyle(logoutDefaultStyle);
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle(logoutHoverStyle));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle(logoutDefaultStyle));
        logoutButton.setOnAction(e -> logout());
        topBar.getChildren().add(logoutButton);

        //setup grid for feature cards
        GridPane cardGrid = new GridPane();
        cardGrid.setHgap(15);
        cardGrid.setVgap(15);

        //create feature cards
        VBox updateAccountCard = makeFeatureCard(
            "Update Account",
            "Modify student and teacher details.",
            "Start",
            e -> openUpdateAccount()
        );

        VBox addDeleteCard = makeFeatureCard(
            "Add/Delete Account",
            "Manage student and teacher accounts.",
            "Manage",
            e -> openAddDelete()
        );

        VBox assignTeacherCard = makeFeatureCard(
            "Assign Teacher",
            "Allocate teachers to specific courses.",
            "Assign",
            e -> openAssignTeacher()
        );

        VBox generateReportCard = makeFeatureCard(
            "Generate Report",
            "Analyze student grades and attendance.",
            "Generate",
            e -> openGenerateReport()
        );

        //add cards to grid
        cardGrid.add(updateAccountCard, 0, 0);
        cardGrid.add(addDeleteCard, 1, 0);
        cardGrid.add(assignTeacherCard, 0, 1);
        cardGrid.add(generateReportCard, 1, 1);

        //combine all elements
        contentArea.getChildren().addAll(topBar, cardGrid);
        pageLayout.getChildren().addAll(sideMenu, contentArea);

        //show the page
        Scene dashboardScene = new Scene(pageLayout, 800, 600);
        mainWindow.setTitle("Admin Dashboard");
        mainWindow.setScene(dashboardScene);
        mainWindow.show();
    }

    //create a card for dashboard features
    private VBox makeFeatureCard(String cardTitle, String description, String buttonText, javafx.event.EventHandler<javafx.event.ActionEvent> buttonAction) {
        //setup card container
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; " +
                     "-fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setPrefWidth(280);
        card.setPrefHeight(180);

        //add card title
        Label titleText = new Label(cardTitle);
        titleText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        //add description
        Text descriptionText = new Text(description);
        descriptionText.setWrappingWidth(250);
        descriptionText.setStyle("-fx-fill: #666;");

        //add action button
        Button cardButton = new Button(buttonText);
        String buttonDefaultStyle = "-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;";
        String buttonHoverStyle = "-fx-background-color: #6790F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;";
        cardButton.setStyle(buttonDefaultStyle);
        cardButton.setOnMouseEntered(e -> cardButton.setStyle(buttonHoverStyle));
        cardButton.setOnMouseExited(e -> cardButton.setStyle(buttonDefaultStyle));
        cardButton.setOnAction(buttonAction);

        //combine all elements
        card.getChildren().addAll(titleText, descriptionText, cardButton);
        return card;
    }

    //navigation methods
    private void openUpdateAccount() {
        new AdminUpdateAccountPage().show(mainWindow, adminUsername);
    }

    private void openAddDelete() {
        new AdminAddDeletePage().show(mainWindow, adminUsername);
    }

    private void openAssignTeacher() {
        new AdminAssignTeacherPage().show(mainWindow, adminUsername);
    }

    private void openGenerateReport() {
        new AdminGenerateReportPage().show(mainWindow, adminUsername);
    }

    private void logout() {
        new LoginPage().show(mainWindow);
    }
} 