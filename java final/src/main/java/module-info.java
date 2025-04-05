module com.example.javafxapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    exports com.example.javafxapp;
    
    opens com.example.javafxapp.model to javafx.base;
    opens com.example.javafxapp.teacher to javafx.base;
    opens com.example.javafxapp.student to javafx.base;
    opens com.example.javafxapp.admin to javafx.base;
} 