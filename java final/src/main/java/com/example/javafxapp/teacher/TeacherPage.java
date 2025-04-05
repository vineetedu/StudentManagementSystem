package com.example.javafxapp.teacher;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import com.example.javafxapp.LoginPage;

public class TeacherPage {
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

        Label title = new Label("Teacher Portal");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label quickAccess = new Label("Quick Access");
        quickAccess.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");

        //sidebar options
        Button viewCoursesBtn = new Button("View Courses");
        Button inputGradesBtn = new Button("Input Grades");
        Button reportAttendanceBtn = new Button("Report Attendance");

        //applying styles to buttons
        for (Button btn : new Button[]{viewCoursesBtn, inputGradesBtn, reportAttendanceBtn}) {
            btn.setPrefWidth(160);
            btn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-padding: 10 20; -fx-background-radius: 6;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color:rgb(219, 221, 247); -fx-text-fill: black; -fx-font-size: 12px; -fx-padding: 10 20; -fx-background-radius: 6;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-padding: 10 20; -fx-background-radius: 6;"));
        }

        //add handlers for sidebar buttons
        viewCoursesBtn.setOnAction(e -> openViewCourses());
        inputGradesBtn.setOnAction(e -> openInputGrades());
        reportAttendanceBtn.setOnAction(e -> openReportAttendance());

        sidebar.getChildren().addAll(title, new Separator(), quickAccess, viewCoursesBtn, 
                                   inputGradesBtn, reportAttendanceBtn);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: white;");

        //top bar with logout
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        Button logoutBtn = new Button("Log Out");
        logoutBtn.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        logoutBtn.setOnAction(e -> logout());
        topBar.getChildren().add(logoutBtn);

        //grid for teacher cards
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        //teacher function cards
        VBox viewCoursesCard = createDashboardCard(
            "View Courses",
            "View your assigned courses and enrolled students.",
            "View Now",
            e -> openViewCourses()
        );

        VBox inputGradesCard = createDashboardCard(
            "Input Grades",
            "Enter and update student grades for your courses.",
            "Enter Grades",
            e -> openInputGrades()
        );

        VBox reportAttendanceCard = createDashboardCard(
            "Report Attendance",
            "Track and report student attendance by course.",
            "Report Now",
            e -> openReportAttendance()
        );

        grid.add(viewCoursesCard, 0, 0);
        grid.add(inputGradesCard, 1, 0);
        grid.add(reportAttendanceCard, 0, 1);

        mainContent.getChildren().addAll(topBar, grid);
        root.getChildren().addAll(sidebar, mainContent);

        Scene mainScene = new Scene(root, 800, 600);
        window.setTitle("Teacher Dashboard");
        window.setScene(mainScene);
        window.show();
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
        actionButton.setOnMouseEntered(e -> actionButton.setStyle("-fx-background-color: #6B7AFF; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        actionButton.setOnMouseExited(e -> actionButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        actionButton.setOnAction(action);

        card.getChildren().addAll(titleLabel, descText, actionButton);
        return card;
    }

    private void openViewCourses() {
        new TeacherViewCoursesPage().show(window, currentUser);
    }

    private void openInputGrades() {
        new TeacherInputGradesPage().show(window, currentUser);
    }

    private void openReportAttendance() {
        new TeacherAttendancePage().show(window, currentUser);
    }

    private void logout() {
        new LoginPage().show(window);
    }
} 