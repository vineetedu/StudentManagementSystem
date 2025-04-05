package com.example.javafxapp.admin;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import com.example.javafxapp.UIUtils;
import com.example.javafxapp.service.TeacherDataService;
import com.example.javafxapp.service.CourseCatalogService;
import com.example.javafxapp.model.TeacherData;

/**
 * this is where admins can assign courses to teachers
 */
public class AdminAssignTeacherPage {
    //main window of the application
    private Stage window;
    
    //keep track of who's logged in
    private String currentUser;
    
    //servicesneed to manage teachers and courses
    private TeacherDataService teacherService = TeacherDataService.getInstance();
    private CourseCatalogService courseService = CourseCatalogService.getInstance();
    
    //UI elements 
    private ListView<String> availableCoursesListView;
    private ListView<String> assignedCoursesListView;
    private ComboBox<String> teacherComboBox;

    //this shows the teacher assignment page 
    public void show(Stage stage, String user) {
        //store the window and user info for later
        this.window = stage;
        this.currentUser = user;

        //setup the main layout 
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #f5f6fa;");

        //add title
        Label pageTitle = new Label("Assign Courses to Teachers");
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        //create the teacher selection section
        VBox teacherSection = createTeacherSection();
        
        //create the course management section
        HBox courseSection = createCourseSection();

        //add a back button
        Button backButton = new Button("Back to Dashboard");
        UIUtils.styleButton(backButton);
        backButton.setOnAction(e -> goBack());

        //put it all together
        mainContainer.getChildren().addAll(pageTitle, teacherSection, courseSection, backButton);
        mainContainer.setAlignment(Pos.TOP_CENTER);

        //show page
        Scene scene = new Scene(mainContainer, 800, 600);
        window.setTitle("Assign Teachers");
        window.setScene(scene);
        window.show();
    }

    //creates the section to pick a teacher
    private VBox createTeacherSection() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);

        //add label
        Label teacherLabel = new Label("Select Teacher:");
        teacherLabel.setStyle("-fx-font-size: 16px;");

        //create the dropdown with all  teachers
        teacherComboBox = new ComboBox<>();
        teacherComboBox.getItems().addAll(teacherService.getAllTeacherUsernames());
        teacherComboBox.setOnAction(e -> updateCourseViews());
        teacherComboBox.setPrefWidth(300);

        //put it together
        section.getChildren().addAll(teacherLabel, teacherComboBox);
        return section;
    }

    //creates the section to manage course assignments
    private HBox createCourseSection() {
        HBox section = new HBox(20);
        section.setAlignment(Pos.CENTER);

        //create the available courses list
        VBox availableCoursesBox = new VBox(10);
        Label availableLabel = new Label("Available Courses");
        availableLabel.setStyle("-fx-font-size: 16px;");
        availableCoursesListView = new ListView<>();
        availableCoursesListView.setPrefSize(300, 300);

        //create the assigned courses list
        VBox assignedCoursesBox = new VBox(10);
        Label assignedLabel = new Label("Assigned Courses");
        assignedLabel.setStyle("-fx-font-size: 16px;");
        assignedCoursesListView = new ListView<>();
        assignedCoursesListView.setPrefSize(300, 300);

        //create buttons to move courses between lists
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        //make some buttons for assigning/removing courses
        Button assignButton = new Button(">");
        Button removeButton = new Button("<");
        UIUtils.styleButton(assignButton);
        UIUtils.styleButton(removeButton);

        //setup what happens when buttons are clicked
        assignButton.setOnAction(e -> assignSelectedCourse());
        removeButton.setOnAction(e -> removeSelectedCourse());

        //put all the buttons together
        buttonBox.getChildren().addAll(assignButton, removeButton);
        buttonBox.setPadding(new Insets(20));

        //put everything together
        availableCoursesBox.getChildren().addAll(availableLabel, availableCoursesListView);
        assignedCoursesBox.getChildren().addAll(assignedLabel, assignedCoursesListView);
        section.getChildren().addAll(availableCoursesBox, buttonBox, assignedCoursesBox);

        return section;
    }

    //updates the course lists when a teacher is selected
    private void updateCourseViews() {
        String selectedTeacher = teacherComboBox.getValue();
        if (selectedTeacher == null) return;

        //get the teacher's current courses
        TeacherData teacher = teacherService.getTeacherData(selectedTeacher);
        
        //clear and update both lists
        availableCoursesListView.getItems().clear();
        assignedCoursesListView.getItems().clear();
        
        //show all available courses that aren't assigned yet
        for (String course : courseService.getAvailableCourses()) {
            if (!teacher.getAssignedCourses().contains(course)) {
                availableCoursesListView.getItems().add(course);
            }
        }
        
        //show all courses currently assigned to this teacher
        assignedCoursesListView.getItems().addAll(teacher.getAssignedCourses());
    }

    //assigns the selected course to the current teacher
    private void assignSelectedCourse() {
        String selectedTeacher = teacherComboBox.getValue();
        String selectedCourse = availableCoursesListView.getSelectionModel().getSelectedItem();
        
        if (selectedTeacher != null && selectedCourse != null) {
            teacherService.assignCourse(selectedTeacher, selectedCourse);
            updateCourseViews();
        }
    }

    //removes the selected course from the current teacher
    private void removeSelectedCourse() {
        String selectedTeacher = teacherComboBox.getValue();
        String selectedCourse = assignedCoursesListView.getSelectionModel().getSelectedItem();
        
        if (selectedTeacher != null && selectedCourse != null) {
            teacherService.removeCourse(selectedTeacher, selectedCourse);
            updateCourseViews();
        }
    }

    //takes us back to the dashboard
    private void goBack() {
        new AdminPage().show(window, currentUser);
    }
} 