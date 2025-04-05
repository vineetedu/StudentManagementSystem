package com.example.javafxapp.admin;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import com.example.javafxapp.LoginPage;
import com.example.javafxapp.UIUtils;

/**
 * this is the main admin dashboard
 */
public class AdminPage {
    //main window of the application
    private Stage window;
    
    //keep track of who's logged in
    private String currentUser;

    //this shows the admin dashboard with all its features
    public void show(Stage stage, String user) {
        //store the window and user info for later
        this.window = stage;
        this.currentUser = user;

        //setup the main layout with a nice background
        HBox pageLayout = new HBox();
        pageLayout.setStyle("-fx-background-color: #f5f6fa;");

        //create a cool sidebar menu on the left
        VBox sideMenu = createSideMenu();

        //create the main content area where all the action happens
        VBox contentArea = createContentArea();

        //put it all together
        pageLayout.getChildren().addAll(sideMenu, contentArea);

        //show our beautiful dashboard
        Scene dashboardScene = new Scene(pageLayout, 800, 600);
        window.setTitle("Admin Dashboard");
        window.setScene(dashboardScene);
        window.show();
    }

    //creates the sidebar menu with navigation buttons
    private VBox createSideMenu() {
        //setup the sidebar container
        VBox sideMenu = new VBox(15);
        sideMenu.setPrefWidth(200);
        sideMenu.setPadding(new Insets(20));
        sideMenu.setStyle("-fx-background-color: #F0F1FF; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        //add title
        Label pageTitle = new Label("Admin Portal");
        pageTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        //create all our menu buttons
        Button dashboardButton = new Button("Dashboard");
        Button updateAccountButton = new Button("Update Account");
        Button assignTeacherButton = new Button("Assign Teacher");
        Button generateReportButton = new Button("Generate Report");

        //make all buttons look consistent
        Button[] menuButtons = {dashboardButton, updateAccountButton, assignTeacherButton, generateReportButton};
        for (Button menuButton : menuButtons) {
            styleMenuButton(menuButton);
        }

        //setup what happens when buttons are clicked
        updateAccountButton.setOnAction(e -> openUpdateAccount());
        assignTeacherButton.setOnAction(e -> openAssignTeacher());
        generateReportButton.setOnAction(e -> openGenerateReport());

        //add everything to the sidebar
        sideMenu.getChildren().addAll(pageTitle, new Separator(), dashboardButton, updateAccountButton, assignTeacherButton, generateReportButton);
        
        return sideMenu;
    }

    //creates the main content area with feature cards
    private VBox createContentArea() {
        //setup the content container
        VBox contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));
        contentArea.setStyle("-fx-background-color: white;");

        //create top bar with logout
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        
        //add a logout button
        Button logoutButton = new Button("Log Out");
        UIUtils.styleButton(logoutButton);
        logoutButton.setOnAction(e -> logout());
        topBar.getChildren().add(logoutButton);

        //create a grid for our feature cards
        GridPane cardGrid = new GridPane();
        cardGrid.setHgap(15);
        cardGrid.setVgap(15);

        //create cards for each feature
        VBox updateAccountCard = makeFeatureCard(
            "Update Account",
            "Modify student and teacher details.",
            "Start",
            e -> openUpdateAccount()
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

        //arrange cards in the grid
        cardGrid.add(updateAccountCard, 0, 0);
        cardGrid.add(assignTeacherCard, 1, 0);
        cardGrid.add(generateReportCard, 0, 1);

        //put everything together
        contentArea.getChildren().addAll(topBar, cardGrid);
        
        return contentArea;
    }

    //styles a menu button 
    private void styleMenuButton(Button button) {
        button.setPrefWidth(160);
        String defaultStyle = "-fx-background-color: white; " +
                            "-fx-text-fill: black; " +
                            "-fx-font-size: 12px; " +
                            "-fx-padding: 10 20; " +
                            "-fx-background-radius: 6;";
        String hoverStyle = "-fx-background-color: rgb(219, 221, 247); " +
                          "-fx-text-fill: black; " +
                          "-fx-font-size: 12px; " +
                          "-fx-padding: 10 20; " +
                          "-fx-background-radius: 6;";
        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));
    }

    //creates a  card foour dashboard features
    private VBox makeFeatureCard(String cardTitle, String description, String buttonText, javafx.event.EventHandler<javafx.event.ActionEvent> buttonAction) {
        //setup the card container
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; " +
                     "-fx-padding: 15; " +
                     "-fx-background-radius: 10; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setPrefWidth(280);
        card.setPrefHeight(180);

        //add a title
        Label titleText = new Label(cardTitle);
        titleText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        //add some descriptive text
        Text descriptionText = new Text(description);
        descriptionText.setWrappingWidth(250);
        descriptionText.setStyle("-fx-fill: #666;");

        //add a button
        Button cardButton = new Button(buttonText);
        UIUtils.styleButton(cardButton);
        cardButton.setOnAction(buttonAction);

        //put the card together
        card.getChildren().addAll(titleText, descriptionText, cardButton);
        return card;
    }

    // handle navigation to different pages
    private void openUpdateAccount() {
        new AdminUpdateAccountPage().show(window, currentUser);
    }

    private void openAssignTeacher() {
        new AdminAssignTeacherPage().show(window, currentUser);
    }

    private void openGenerateReport() {
        new AdminGenerateReportPage().show(window, currentUser);
    }

    private void logout() {
        new LoginPage().show(window);
    }
} 