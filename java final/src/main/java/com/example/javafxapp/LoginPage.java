package com.example.javafxapp;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import com.example.javafxapp.admin.AdminPage;
import com.example.javafxapp.service.AuthService;
import com.example.javafxapp.teacher.TeacherPage;
import com.example.javafxapp.student.StudentPage;

import javafx.geometry.Insets;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;

//page that handles user login
public class LoginPage {
    
    //shows the login page
    public void show(Stage primaryStage) {
        //setup the main container with white background
        HBox mainContainer = new HBox();
        mainContainer.setStyle("-fx-background-color: white;");

        //create the left side panel
        Rectangle leftPanel = new Rectangle(300, 600);
        leftPanel.setFill(javafx.scene.paint.Color.valueOf("#F0F1FF"));
        StackPane leftSideContainer = new StackPane(leftPanel);
        
        //create the right side login form
        VBox loginForm = new VBox(12);
        loginForm.setAlignment(Pos.CENTER_RIGHT);
        loginForm.setPadding(new Insets(30, 100, 20, 100));
        loginForm.setStyle("-fx-background-color: white;");

        //setup the user type dropdown
        Label userTypeLabel = new Label("Select User Type:");
        ComboBox<String> userTypeDropdown = new ComboBox<>();
        userTypeDropdown.getItems().addAll("Student", "Teacher", "Admin");
        userTypeDropdown.setValue("Student");
        
        //style the dropdown
        userTypeDropdown.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #5664F5;" +
            "-fx-border-radius: 6px;"
        );
        userTypeDropdown.getStyleClass().add("no-scroll-combo-box");
        
        //customize dropdown items appearance
        userTypeDropdown.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
                    
                    //add hover effects
                    setOnMouseEntered(e -> setStyle(
                        "-fx-background-color: #F0F1FF;" +
                        "-fx-border-color: #5664F5;" +
                        "-fx-border-width: 1px;" +
                        "-fx-text-fill: black;"
                    ));
                    setOnMouseExited(e -> setStyle("-fx-background-color: transparent; -fx-text-fill: black;"));
                }
            }
        });

        //create the page title
        Label pageTitle = new Label("Student Portal Login");
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        //setup username field
        Label usernameLabel = new Label("Enter Username:");
        TextField usernameInput = new TextField();
        String defaultFieldStyle = "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-radius: 6px; -fx-background-radius: 6px;";
        String focusedFieldStyle = defaultFieldStyle + "; -fx-border-color: #5664F5; -fx-border-width: 1px;";
        usernameInput.setStyle(defaultFieldStyle);
        
        //add focus effects to username field
        usernameInput.focusedProperty().addListener((obs, oldValue, newValue) -> {
            usernameInput.setStyle(newValue ? focusedFieldStyle : defaultFieldStyle);
        });

        //setup password field
        Label passwordLabel = new Label("Enter Password:");
        PasswordField passwordInput = new PasswordField();
        passwordInput.setStyle(defaultFieldStyle);
        
        //add focus effects to password field
        passwordInput.focusedProperty().addListener((obs, oldValue, newValue) -> {
            passwordInput.setStyle(newValue ? focusedFieldStyle : defaultFieldStyle);
        });

        //create the login button
        Button loginButton = new Button("Login");
        UIUtils.styleButton(loginButton);

        //handle login button click
        loginButton.setOnAction(e -> {
            String selectedUserType = userTypeDropdown.getValue();
            String username = usernameInput.getText();
            String password = passwordInput.getText();

            //check if login is valid
            if (!AuthService.validateCredentials(selectedUserType, username, password)) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Login Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Invalid username or password for the selected user type.");
                errorAlert.showAndWait();
                return;
            }

            //go to appropriate page based on user type
            switch(selectedUserType) {
                case "Student":
                    new StudentPage().show(primaryStage, username);
                    break;
                case "Teacher":
                    new TeacherPage().show(primaryStage, username);
                    break;
                case "Admin":
                    new AdminPage().show(primaryStage, username);
                    break;
            }
        });

        //set sizes for input fields
        usernameInput.setPrefWidth(200);
        usernameInput.setPrefHeight(30);
        passwordInput.setPrefWidth(200);
        passwordInput.setPrefHeight(30);
        userTypeDropdown.setPrefWidth(200);

        //align all labels to the left
        pageTitle.setAlignment(Pos.CENTER_LEFT);
        pageTitle.setMaxWidth(Double.MAX_VALUE);
        userTypeLabel.setAlignment(Pos.CENTER_LEFT);
        userTypeLabel.setMaxWidth(Double.MAX_VALUE);
        usernameLabel.setAlignment(Pos.CENTER_LEFT);
        usernameLabel.setMaxWidth(Double.MAX_VALUE);
        passwordLabel.setAlignment(Pos.CENTER_LEFT);
        passwordLabel.setMaxWidth(Double.MAX_VALUE);

        //create containers for dropdown and login button
        HBox dropdownContainer = new HBox();
        dropdownContainer.setAlignment(Pos.CENTER_LEFT);
        dropdownContainer.getChildren().add(userTypeDropdown);

        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER_LEFT);
        buttonContainer.getChildren().add(loginButton);

        //add spacing around title and button
        VBox.setMargin(pageTitle, new Insets(0, 0, 25, 0));
        VBox.setMargin(buttonContainer, new Insets(15, 0, 0, 0));

        //add all elements to the form
        loginForm.getChildren().addAll(
            pageTitle,
            userTypeLabel,
            dropdownContainer,
            usernameLabel,
            usernameInput,
            passwordLabel,
            passwordInput,
            buttonContainer
        );

        //combine left panel and login form
        mainContainer.getChildren().addAll(leftSideContainer, loginForm);

        //create and show the scene
        Scene loginScene = new Scene(mainContainer, 800, 600);
        loginScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setTitle("Student Portal Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
} 