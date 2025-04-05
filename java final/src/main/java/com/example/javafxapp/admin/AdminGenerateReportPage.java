package com.example.javafxapp.admin;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.javafxapp.service.StudentDataService;
import com.example.javafxapp.service.TeacherDataService;
import com.example.javafxapp.model.StudentData;
import com.example.javafxapp.model.TeacherData;
import com.example.javafxapp.model.EnrollmentEntry;
import com.example.javafxapp.model.CourseEntry;
import com.example.javafxapp.UIUtils;
import java.util.List;
import java.util.Map;

/**
 * this is where the admin can generate reports about students and courses
 * 
 * i spent way too long making these tables look nice
 * but at least they work now :)
 */
public class AdminGenerateReportPage {
    //main window of the application
    private Stage window;
    
    //keep track of who's logged in
    private String currentUser;
    
    //we need these to get all the data
    private StudentDataService studentDataService;
    private TeacherDataService teacherDataService;

    //this shows the report generation page with all its glory
    public void show(Stage stage, String user) {
        //store the window and user info for later
        this.window = stage;
        this.currentUser = user;
        
        //get our data services ready
        this.studentDataService = StudentDataService.getInstance();
        this.teacherDataService = TeacherDataService.getInstance();

        //make the main container look nice
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: white;"); //white background looks clean
        container.setPadding(new Insets(20));

        //add a fancy title at the top
        Label titleLabel = new Label("Generate Reports");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        //create tabs to organize different reports
        TabPane tabPane = new TabPane();
        tabPane.setPrefWidth(700); //make it nice and wide

        //first tab: student enrollments
        Tab studentTab = createStudentEnrollmentsTab();
        
        //second tab: teacher's courses
        Tab teacherTab = createTeacherCoursesTab();

        //add both tabs to the pane
        tabPane.getTabs().addAll(studentTab, teacherTab);

        //add a back button to return to dashboard
        Button backButton = new Button("Back to Dashboard");
        UIUtils.styleButton(backButton); //make it match other buttons
        backButton.setOnAction(e -> goBackToDashboard(stage, user));

        //put it all together
        container.getChildren().addAll(titleLabel, tabPane, backButton);

        //show the page created
        Scene scene = new Scene(container, 800, 600);
        window.setTitle("Admin - Generate Reports");
        window.setScene(scene);
        window.show();
    }

    //creates the student enrollments tab
    private Tab createStudentEnrollmentsTab() {
        Tab studentTab = new Tab("Student Enrollments");
        studentTab.setClosable(false); //dont let them close tab
        
        //container for the tab content
        VBox studentTabContent = new VBox(10);
        studentTabContent.setPadding(new Insets(10));

        //create the table to show student data
        TableView<EnrollmentEntry> studentTable = new TableView<>();
        studentTable.setPrefHeight(300);

        //column for student names
        TableColumn<EnrollmentEntry, String> studentNameColumn = new TableColumn<>("Student Name");
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentNameColumn.setPrefWidth(200);

        //column for usernames
        TableColumn<EnrollmentEntry, String> studentUsernameColumn = new TableColumn<>("Username");
        studentUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        studentUsernameColumn.setPrefWidth(150);

        //column for enrolled courses
        TableColumn<EnrollmentEntry, String> enrolledCoursesColumn = new TableColumn<>("Enrolled Courses");
        enrolledCoursesColumn.setCellValueFactory(new PropertyValueFactory<>("enrolledCourses"));
        enrolledCoursesColumn.setPrefWidth(350);

        //add all columns to the table
        studentTable.getColumns().addAll(studentNameColumn, studentUsernameColumn, enrolledCoursesColumn);
        
        //load up the student data
        ObservableList<EnrollmentEntry> studentData = loadStudentEnrollments();
        studentTable.setItems(studentData);

        //add table to the tab
        studentTabContent.getChildren().add(studentTable);
        studentTab.setContent(studentTabContent);
        
        return studentTab;
    }

    //creates the teacher courses tab
    private Tab createTeacherCoursesTab() {
        Tab teacherTab = new Tab("Teacher Courses");
        teacherTab.setClosable(false); //keep this one open too
        
        //container for the tab content
        VBox teacherTabContent = new VBox(10);
        teacherTabContent.setPadding(new Insets(10));

        //create the table to show course data
        TableView<CourseEntry> teacherTable = new TableView<>();
        teacherTable.setPrefHeight(300);

        //column for course names
        TableColumn<CourseEntry, String> courseColumn = new TableColumn<>("Course");
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        courseColumn.setPrefWidth(200);

        //column for number of students
        TableColumn<CourseEntry, String> studentCountColumn = new TableColumn<>("Number of Students");
        studentCountColumn.setCellValueFactory(new PropertyValueFactory<>("studentCount"));
        studentCountColumn.setPrefWidth(150);

        //column for list of enrolled students
        TableColumn<CourseEntry, String> studentListColumn = new TableColumn<>("Enrolled Students");
        studentListColumn.setCellValueFactory(new PropertyValueFactory<>("studentList"));
        studentListColumn.setPrefWidth(350);

        //add all columns to the table
        teacherTable.getColumns().addAll(courseColumn, studentCountColumn, studentListColumn);
        
        //load up the course data
        ObservableList<CourseEntry> teacherData = loadTeacherCourses();
        teacherTable.setItems(teacherData);

        //add table to the tab
        teacherTabContent.getChildren().add(teacherTable);
        teacherTab.setContent(teacherTabContent);
        
        return teacherTab;
    }

    //loads all student enrollment data for the table
    private ObservableList<EnrollmentEntry> loadStudentEnrollments() {
        //create a list to hold entries
        ObservableList<EnrollmentEntry> entries = FXCollections.observableArrayList();
        
        //get all students from the service
        Map<String, StudentData> students = studentDataService.getAllStudents();
        
        //convert each student's data into a table entry
        for (Map.Entry<String, StudentData> entry : students.entrySet()) {
            String username = entry.getKey();
            StudentData student = entry.getValue();
            
            //combine all courses into one string with commas
            String enrolledCourses = String.join(", ", student.getEnrolledCourses());
            
            //create and add the entry
            entries.add(new EnrollmentEntry(
                student.getFullName(),
                username,
                enrolledCourses
            ));
        }

        return entries;
    }

    //loads all course data into a nice format for the table
    private ObservableList<CourseEntry> loadTeacherCourses() {
        //create a list to hold our entries
        ObservableList<CourseEntry> entries = FXCollections.observableArrayList();
        
        //get the teacher's data
        TeacherData teacher = teacherDataService.getTeacherData("teacher");
        
        //make sure got the teacher data
        if (teacher != null) {
            //get all courses
            List<String> courses = teacher.getAssignedCourses();
            
            //convert each course's data into a table entry
            for (String course : courses) {
                //get students in this course
                List<String> students = teacher.getStudentsInCourse(course);
                
                //combine all student names into one string with commas
                String studentList = String.join(", ", students);
                
                //create and add the entry
                entries.add(new CourseEntry(
                    course,
                    String.valueOf(students.size()),
                    studentList
                ));
            }
        }
        
        return entries;
    }
    
    //go back to the admin dashboard
    private void goBackToDashboard(Stage stage, String user) {
        new AdminPage().show(stage, user);
    }
} 