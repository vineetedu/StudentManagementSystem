package com.example.javafxapp;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import com.example.javafxapp.admin.AdminPage;
import com.example.javafxapp.teacher.TeacherPage;

import javafx.geometry.Insets;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;

public class LoginPage {
    public void show(Stage primaryStage) {
        //MAIN SETUP
        //main container
        HBox mainContainer = new HBox();
        mainContainer.setStyle("-fx-background-color: white;");

        //left rectangle in accent color
        Rectangle leftRectangle = new Rectangle(300, 600); //width and height
        leftRectangle.setFill(javafx.scene.paint.Color.valueOf("#F0F1FF"));
        StackPane leftPane = new StackPane(leftRectangle);
        
        //right side with login content
        VBox vbox = new VBox(12);
        vbox.setAlignment(Pos.CENTER_RIGHT);
        vbox.setPadding(new Insets(30, 100, 20, 100)); //padding
        vbox.setStyle("-fx-background-color: white;");
        //MAIN SETUP END

        //COMBO BOX
        //user type selection
        Label typeLabel = new Label("Select User Type:");
        ComboBox<String> userTypeCombo = new ComboBox<>();
        userTypeCombo.getItems().addAll("Student", "Teacher", "Admin");
        userTypeCombo.setValue("Student");
        
        //css for combo box
        userTypeCombo.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #5664F5;" +
            "-fx-border-radius: 6px;"
        );
        
        //add style class to remove scrollbar
        userTypeCombo.getStyleClass().add("no-scroll-combo-box");
        
        //styling cells based off of mouse interactions
        userTypeCombo.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
                    
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
        //END OF COMBO BOX

        //TEXT FIELDS
        //title display
        Label titleLabel = new Label("Student Portal Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        //username display
        Label usernameLabel = new Label("Enter Username:");
        TextField usernameField = new TextField();
        usernameField.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-radius: 6px; -fx-background-radius: 6px;");
        usernameField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) { //if selected change colour
                usernameField.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-color: #5664F5; -fx-border-width: 1px; -fx-border-radius: 6px; -fx-background-radius: 6px;");
            } else { //if not selected change colour to default
                usernameField.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-color: transparent; -fx-border-radius: 6px; -fx-background-radius: 6px;");
            }
        });

        //password display
        Label passwordLabel = new Label("Enter Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-radius: 6px; -fx-background-radius: 6px;");
        passwordField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) { //if selected change colour
                passwordField.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-color: #5664F5; -fx-border-width: 1px; -fx-border-radius: 6px; -fx-background-radius: 6px;");
            } else { //if not selected change colour to default
                passwordField.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-border-color: transparent; -fx-border-radius: 6px; -fx-background-radius: 6px;");
            }
        });
        //END OF TEXT FIELDS

        //LOGIN BUTTON
        //login button setup
        Button loginButton = new Button("Login");
        UIUtils.styleButton(loginButton);

        //when press login store data and navigate to appropriate page
        loginButton.setOnAction(e -> {
            String userType = userTypeCombo.getValue();
            String username = usernameField.getText();
            String password = passwordField.getText();

            //if username or password is empty
            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR); // produce error
                alert.setTitle("Login Error"); //error title
                alert.setHeaderText(null);
                alert.setContentText("Enter username and password."); //error message
                alert.showAndWait();
                return; //stop login
            }

            //if both are filled out, login
            switch(userType) {
                case "Student":
                    new com.example.javafxapp.student.StudentPage().show(primaryStage, username);
                    break;
                case "Teacher":
                    new TeacherPage().show(primaryStage, username);
                    break;
                case "Admin":
                    new AdminPage().show(primaryStage, username);
                    break;
            }
        });
        //END OF LOGIN BUTTON

        //aligning any interactive elements
        usernameField.setPrefWidth(200);
        usernameField.setPrefHeight(30);
        passwordField.setPrefWidth(200);
        passwordField.setPrefHeight(30);
        userTypeCombo.setPrefWidth(200);

        titleLabel.setAlignment(Pos.CENTER_LEFT);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        typeLabel.setAlignment(Pos.CENTER_LEFT);
        typeLabel.setMaxWidth(Double.MAX_VALUE);
        usernameLabel.setAlignment(Pos.CENTER_LEFT);
        usernameLabel.setMaxWidth(Double.MAX_VALUE);
        passwordLabel.setAlignment(Pos.CENTER_LEFT);
        passwordLabel.setMaxWidth(Double.MAX_VALUE);

        //hbox to align login button and combobox leftwards
        HBox userTypeBox = new HBox();
        userTypeBox.setAlignment(Pos.CENTER_LEFT);
        userTypeBox.getChildren().add(userTypeCombo);

        HBox loginButtonBox = new HBox();
        loginButtonBox.setAlignment(Pos.CENTER_LEFT);
        loginButtonBox.getChildren().add(loginButton);

        //margins
        VBox.setMargin(titleLabel, new Insets(0, 0, 25, 0));
        VBox.setMargin(loginButtonBox, new Insets(15, 0, 0, 0));

        vbox.getChildren().addAll(
            titleLabel,
            typeLabel,
            userTypeBox,
            usernameLabel,
            usernameField,
            passwordLabel,
            passwordField,
            loginButtonBox
        );

        //adding both sides to the main container
        mainContainer.getChildren().addAll(leftPane, vbox);

        Scene scene = new Scene(mainContainer, 800, 600);

        //adding the stylesheet
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setTitle("Student Portal Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
} 