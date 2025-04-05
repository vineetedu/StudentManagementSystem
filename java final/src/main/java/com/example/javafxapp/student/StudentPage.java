package com.example.javafxapp.student;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import com.example.javafxapp.LoginPage;

/**
 * main dashboard for students to access their academic information and tools.
 */
public class StudentPage {
    private Stage mainWindow;
    private String studentUsername;

    public void show(Stage stage, String user) {
        this.mainWindow = stage;
        this.studentUsername = user;

        //setup main layout with sidebar and content area
        HBox pageLayout = new HBox();
        pageLayout.setStyle("-fx-background-color: #f5f6fa;");

        //create navigation sidebar
        VBox sideMenu = new VBox(15);
        sideMenu.setPrefWidth(200);
        sideMenu.setPadding(new Insets(20));
        sideMenu.setStyle("-fx-background-color: #F0F1FF; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        //add page title and menu header
        Label pageTitle = new Label("Student Portal");
        pageTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label menuHeader = new Label("Quick Access");
        menuHeader.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");

        //create navigation buttons
        Button dashboardButton = new Button("Dashboard");
        Button enrollButton = new Button("View Courses");
        Button gradesButton = new Button("View Records");
        Button assignmentsButton = new Button("Assignments");
        Button profileButton = new Button("Edit Details");

        //style navigation buttons
        for (Button btn : new Button[]{dashboardButton, enrollButton, gradesButton, assignmentsButton, profileButton}) {
            btn.setPrefWidth(160);
            btn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 6;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color:rgb(219, 221, 247); -fx-text-fill: black; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 6;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 6;"));
        }

        //setup button actions
        enrollButton.setOnAction(e -> openEnrollCourses());
        gradesButton.setOnAction(e -> openGradesAttendance());
        assignmentsButton.setOnAction(e -> openAssignments());
        profileButton.setOnAction(e -> openPersonalDetails());

        //add all elements to sidebar
        sideMenu.getChildren().addAll(pageTitle, new Separator(), menuHeader, dashboardButton, enrollButton, gradesButton, assignmentsButton, profileButton);

        //create main content area
        VBox contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));
        contentArea.setStyle("-fx-background-color: white;");

        //add top bar with logout button
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        Button logoutButton = new Button("Log Out");
        logoutButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: #6790F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        logoutButton.setOnAction(e -> logout());
        topBar.getChildren().add(logoutButton);

        //setup grid for feature cards
        GridPane cardGrid = new GridPane();
        cardGrid.setHgap(15);
        cardGrid.setVgap(15);

        //create feature cards
        VBox enrollCard = createDashboardCard(
            "Enroll in Courses",
            "Browse and enroll in available courses.",
            "View Courses",
            e -> openEnrollCourses()
        );

        VBox gradesCard = createDashboardCard(
            "Grades & Attendance",
            "Check your academic performance and attendance.",
            "View Records",
            e -> openGradesAttendance()
        );

        VBox assignmentsCard = createDashboardCard(
            "Assignments",
            "View and submit your assignments.",
            "View Assignments",
            e -> openAssignments()
        );

        VBox profileCard = createDashboardCard(
            "Personal Details",
            "Update your personal information.",
            "Edit Details",
            e -> openPersonalDetails()
        );

        //add cards to grid
        cardGrid.add(enrollCard, 0, 0);
        cardGrid.add(gradesCard, 1, 0);
        cardGrid.add(assignmentsCard, 0, 1);
        cardGrid.add(profileCard, 1, 1);

        //combine all elements
        contentArea.getChildren().addAll(topBar, cardGrid);
        pageLayout.getChildren().addAll(sideMenu, contentArea);

        //show the page
        Scene dashboardScene = new Scene(pageLayout, 800, 600);
        mainWindow.setTitle("Student Dashboard");
        mainWindow.setScene(dashboardScene);
        mainWindow.show();
    }

    private VBox createDashboardCard(String title, String desc, String btnText, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; " +
                      "-fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setPrefWidth(280);
        card.setPrefHeight(180);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Text descText = new Text(desc);
        descText.setWrappingWidth(250);
        descText.setStyle("-fx-fill: #666;");

        Button actionButton = new Button(btnText);
        actionButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        actionButton.setOnMouseEntered(e -> actionButton.setStyle("-fx-background-color: #6790F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        actionButton.setOnMouseExited(e -> actionButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        actionButton.setOnAction(action);

        card.getChildren().addAll(titleLabel, descText, actionButton);
        return card;
    }

    //navigation methods
    private void openEnrollCourses() {
        new StudentEnrollCoursesPage().show(mainWindow, studentUsername);
    }

    private void openGradesAttendance() {
        new StudentGradesAttendancePage().show(mainWindow, studentUsername);
    }

    private void openAssignments() {
        new StudentAssignmentsPage().show(mainWindow, studentUsername);
    }

    private void openPersonalDetails() {
        new StudentPersonalDetailsPage().show(mainWindow, studentUsername);
    }

    private void logout() {
        new LoginPage().show(mainWindow);
    }
}
