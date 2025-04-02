package com.example.javafxapp.student;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import com.example.javafxapp.LoginPage;

public class StudentPage {
    private Stage window;
    private String currentUser;

    public void show(Stage stage, String user) {
        this.window = stage;
        this.currentUser = user;

        HBox root = new HBox();
        root.setStyle("-fx-background-color: #f5f6fa;");

        //sidebar and titles
        VBox sidebar = new VBox(15);
        sidebar.setPrefWidth(200);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #F0F1FF; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label title = new Label("Student Portal"); //title of sidebar
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label menuHeader = new Label("Quick Access"); //header of sidebar
        menuHeader.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");

        //sidebar buttons for dashboard, profile, settings
        Button dashboardBtn = new Button("Dashboard");
        Button profileBtn = new Button("Profile");
        Button settingsBtn = new Button("Settings");

        //applying styles to buttons
        for (Button btn : new Button[]{dashboardBtn, profileBtn, settingsBtn}) {
            btn.setPrefWidth(160);
            btn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 6;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color:rgb(219, 221, 247); -fx-text-fill: black; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 6;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 6;"));
        }

        //event handlers for buttons on sidebar
        profileBtn.setOnAction(e -> openProfile());
        settingsBtn.setOnAction(e -> openSettings());

        sidebar.getChildren().addAll(title, new Separator(), menuHeader, dashboardBtn, profileBtn, settingsBtn);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: white;");

        //top bar and logout button 
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        Button logoutBtn = new Button("Log Out");
        logoutBtn.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: #6790F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        logoutBtn.setOnAction(e -> logout());
        topBar.getChildren().add(logoutBtn);

        //setting up the grid and spacing for cards // options for user
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        //dashboard cards for each option, grades, attendance, messages, profile
        VBox gradesCard = createDashboardCard("Grades", "Check your latest academic performance.", "View Grades", e -> openGrades());
        VBox attendanceCard = createDashboardCard("Attendance", "View your class attendance records.", "Check Attendance", e -> openAttendance());
        VBox messagesCard = createDashboardCard("Messages", "Stay updated with new notifications.", "Open Messages", e -> openNotifications());
        VBox profileCard = createDashboardCard("Profile", "Update your personal details.", "Edit Profile", e -> openProfile());

        grid.add(gradesCard, 0, 0);
        grid.add(attendanceCard, 1, 0);
        grid.add(messagesCard, 0, 1);
        grid.add(profileCard, 1, 1);

        //adding all elements of the ui together
        mainContent.getChildren().addAll(topBar, grid);
        root.getChildren().addAll(sidebar, mainContent);

        //mainscene setup with size and title
        Scene mainScene = new Scene(root, 800, 600);
        window.setTitle("Student Dashboard");
        window.setScene(mainScene);
        window.show();
    }

    //card template for styles and height so everything is consistent
    private VBox createDashboardCard(String title, String desc, String btnText, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        VBox card = new VBox(10); //card template
        card.setStyle("-fx-background-color: white; -fx-padding: 15; " +
                      "-fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setPrefWidth(280);
        card.setPrefHeight(180);

        Label titleLabel = new Label(title); //title of card
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Text descText = new Text(desc); //description of card
        descText.setWrappingWidth(250);
        descText.setStyle("-fx-fill: #666;");

        Button actionButton = new Button(btnText); //button for card
        actionButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        actionButton.setOnMouseEntered(e -> actionButton.setStyle("-fx-background-color: #6790F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        actionButton.setOnMouseExited(e -> actionButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        actionButton.setOnAction(action);

        card.getChildren().addAll(titleLabel, descText, actionButton);
        return card;
    }

    //button actions and prints to check for success
    private void openProfile() {
        System.out.println("opening profile");
        new StudentProfilePage().show(window, currentUser);
    }

    private void openSettings() {
        System.out.println("opening settings");
        new StudentSettingsPage().show(window, currentUser);
    }

    private void openGrades() {
        System.out.println("opening grades");
        new StudentGradesPage().show(window, currentUser);
    }

    private void openAttendance() {
        System.out.println("opening attendance");
        new StudentAttendancePage().show(window, currentUser);
    }

    private void openNotifications() {
        System.out.println("opening notifications");
        new StudentNotificationsPage().show(window, currentUser);
    }

    private void logout() {
        System.out.println("opening login page");
        new LoginPage().show(window);
    }
}
