package com.example.javafxapp.admin;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import com.example.javafxapp.LoginPage;

public class AdminPage {
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

        Label title = new Label("Admin Portal");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        //sidebar options
        Button updateAccountBtn = new Button("Update Account");
        Button addDeleteBtn = new Button("Add/Delete Account");
        Button assignTeacherBtn = new Button("Assign Teacher");
        Button generateReportBtn = new Button("Generate Report");

        //applying styles to buttons
        for (Button btn : new Button[]{updateAccountBtn, addDeleteBtn, assignTeacherBtn, generateReportBtn}) {
            btn.setPrefWidth(160);
            btn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-padding: 10 20; -fx-background-radius: 6;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color:rgb(219, 221, 247); -fx-text-fill: black; -fx-font-size: 12px; -fx-padding: 10 20; -fx-background-radius: 6;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 12px; -fx-padding: 10 20; -fx-background-radius: 6;"));
        }

        // Add click handlers for sidebar buttons
        updateAccountBtn.setOnAction(e -> openUpdateAccount());
        addDeleteBtn.setOnAction(e -> openAddDelete());
        assignTeacherBtn.setOnAction(e -> openAssignTeacher());
        generateReportBtn.setOnAction(e -> openGenerateReport());

        sidebar.getChildren().addAll(title, new Separator(), updateAccountBtn, addDeleteBtn, assignTeacherBtn, generateReportBtn);

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle("-fx-background-color: white;");

        //top bar with logout
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        Button logoutBtn = new Button("Log Out");
        logoutBtn.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: #6790F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        logoutBtn.setOnAction(e -> logout());
        topBar.getChildren().add(logoutBtn);

        //grid for admin cards
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        //admin function cards
        VBox updateAccountCard = createDashboardCard(
            "Update Account",
            "Modify student and teacher details.",
            "Start",
            e -> openUpdateAccount()
        );

        VBox addDeleteCard = createDashboardCard(
            "Add/Delete Account",
            "Manage student and teacher accounts.",
            "Manage",
            e -> openAddDelete()
        );

        VBox assignTeacherCard = createDashboardCard(
            "Assign Teacher",
            "Allocate teachers to specific courses.",
            "Assign",
            e -> openAssignTeacher()
        );

        VBox generateReportCard = createDashboardCard(
            "Generate Report",
            "Analyze student grades and attendance.",
            "Generate",
            e -> openGenerateReport()
        );

        grid.add(updateAccountCard, 0, 0);
        grid.add(addDeleteCard, 1, 0);
        grid.add(assignTeacherCard, 0, 1);
        grid.add(generateReportCard, 1, 1);

        mainContent.getChildren().addAll(topBar, grid);
        root.getChildren().addAll(sidebar, mainContent);

        Scene mainScene = new Scene(root, 800, 600);
        window.setTitle("Admin Dashboard");
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
        actionButton.setOnMouseEntered(e -> actionButton.setStyle("-fx-background-color: #6790F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        actionButton.setOnMouseExited(e -> actionButton.setStyle("-fx-background-color: #5664F5; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"));
        actionButton.setOnAction(action);

        card.getChildren().addAll(titleLabel, descText, actionButton);
        return card;
    }

    private void openUpdateAccount() {
        System.out.println("Opening update account page");
        new AdminUpdateAccountPage().show(window, currentUser);
    }

    private void openAddDelete() {
        System.out.println("Opening add/delete account page");
        new AdminAddDeletePage().show(window, currentUser);
    }

    private void openAssignTeacher() {
        System.out.println("Opening assign teacher page");
        new AdminAssignTeacherPage().show(window, currentUser);
    }

    private void openGenerateReport() {
        System.out.println("Opening generate report page");
        new AdminGenerateReportPage().show(window, currentUser);
    }

    private void logout() {
        System.out.println("Logging out to login page");
        new LoginPage().show(window);
    }
} 