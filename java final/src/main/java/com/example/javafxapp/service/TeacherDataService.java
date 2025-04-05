package com.example.javafxapp.service;

import com.example.javafxapp.model.TeacherData;
import com.example.javafxapp.model.StudentData;
import com.example.javafxapp.model.Assignment;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * manages teacher data and academic operations
 */
public class TeacherDataService {
    //singleton instance
    private static TeacherDataService instance;
    
    //teacher data storage
    private Map<String, TeacherData> teachers;
    
    //references to other services
    private StudentDataService studentDataService;
    private CourseCatalogService courseCatalogService;

    //private constructor for pattern
    private TeacherDataService() {
        teachers = new HashMap<>();
        studentDataService = StudentDataService.getInstance();
        courseCatalogService = CourseCatalogService.getInstance();
        initializeTeacherData();
        syncWithStudentData();
    }

    //get singleton instance
    public static TeacherDataService getInstance() {
        if (instance == null) {
            instance = new TeacherDataService();
        }
        return instance;
    }

    //create and initialize teacher account with sample data
    private void initializeTeacherData() {
        TeacherData teacher = new TeacherData("teacher");
        teacher.setFullName("Professor Smith");
        teacher.setEmail("teacher@school.edu");
        
        //assign all available courses to teacher
        for (String course : courseCatalogService.getAvailableCourses()) {
            teacher.addCourse(course);
        }

        teachers.put("teacher", teacher);
    }

    //sync teacher's course enrollments with student data
    public void syncWithStudentData() {
        TeacherData teacherData = teachers.get("teacher");
        
        if (teacherData == null) {
            return;
        }
        
        StudentDataService studentService = StudentDataService.getInstance();
        Map<String, StudentData> allStudents = studentService.getAllStudents();
        
        //update course enrollments for each course
        for (String course : teacherData.getAssignedCourses()) {
            for (Map.Entry<String, StudentData> entry : allStudents.entrySet()) {
                String studentUsername = entry.getKey();
                StudentData studentData = entry.getValue();
                
                if (studentData.getEnrolledCourses().contains(course)) {
                    if (!teacherData.getStudentsInCourse(course).contains(studentUsername)) {
                        teacherData.addStudentToCourse(course, studentUsername);
                    }
                }
            }
        }
    }

    //retrieve teacher's academic records
    public TeacherData getTeacherData(String username) {
        if (!teachers.containsKey("teacher")) {
            initializeTeacherData();
        }
        
        addSampleStudentsToCourses();
        syncWithStudentData();
        
        return teachers.get("teacher");
    }

    //update teacher's academic records
    public void updateTeacherData(TeacherData teacherData) {
        if ("teacher".equals(teacherData.getUsername())) {
            teachers.put(teacherData.getUsername(), teacherData);
        }
    }

    //enroll a student in one of teacher's courses
    public void addStudentToCourse(String teacherUsername, String course, String studentUsername) {
        if ("teacher".equals(teacherUsername)) {
            TeacherData teacher = teachers.get(teacherUsername);
            
            if (teacher != null) {
                teacher.addStudentToCourse(course, studentUsername);
            }
        }
    }

    //record a student's grade for an assignment
    public void updateStudentGrade(String teacherUsername, String course, String studentUsername, String assignmentTitle, double grade) {
        if (grade < 0 || grade > 100) {
            return;
        }
        
        if ("teacher".equals(teacherUsername)) {
            TeacherData teacher = teachers.get(teacherUsername);
            
            if (teacher != null) {
                teacher.updateGrade(course, studentUsername, grade);
                
                StudentData studentData = studentDataService.getStudentData(studentUsername);
                
                if (studentData != null) {
                    List<Assignment> assignments = studentData.getAssignments();
                    if (assignments != null) {
                        for (Assignment assignment : assignments) {
                            if (assignment.getCourse().equals(course) && 
                                assignment.getTitle().equals(assignmentTitle) && 
                                assignment.isSubmitted()) {
                                assignment.setGraded(true);
                                assignment.setScore(grade);
                                studentDataService.updateStudentData(studentData);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    //record student attendance for today
    public void markStudentAttendance(String teacherUsername, String course, String studentUsername, boolean present) {
        if ("teacher".equals(teacherUsername)) {
            TeacherData teacher = teachers.get(teacherUsername);
            
            if (teacher != null) {
                teacher.markAttendance(course, studentUsername, present);
                
                StudentData studentData = studentDataService.getStudentData(studentUsername);
                
                if (studentData != null) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    String today = sdf.format(new java.util.Date());
                    
                    studentData.addAttendanceRecord(course, today, present);
                    studentDataService.updateStudentData(studentData);
                }
            }
        }
    }
    
    //record student attendance for a specific date
    public void markStudentAttendanceWithDate(String teacherUsername, String course, String studentUsername, String date, boolean present) {
        if ("teacher".equals(teacherUsername)) {
            TeacherData teacher = teachers.get(teacherUsername);
            
            if (teacher != null) {
                teacher.markAttendance(course, studentUsername, present);
                
                StudentData studentData = studentDataService.getStudentData(studentUsername);
                
                if (studentData != null) {
                    studentData.addAttendanceRecord(course, date, present);
                    studentDataService.updateStudentData(studentData);
                }
            }
        }
    }
    
    //get list of students enrolled in a course
    public List<String> getStudentsInCourse(String course) {
        TeacherData teacher = teachers.get("teacher");
        
        if (teacher != null && teacher.getCourseStudents().containsKey(course)) {
            return teacher.getCourseStudents().get(course);
        }
        
        return new ArrayList<>();
    }

    //add sample students to courses for testing purposes
    public void addSampleStudentsToCourses() {
        TeacherData teacherData = teachers.get("teacher");
        
        if (teacherData == null) {
            return;
        }
        
        List<String> teacherCourses = teacherData.getAssignedCourses();
        
        if (teacherCourses.isEmpty()) {
            return;
        }
        
        StudentDataService studentService = StudentDataService.getInstance();
        Map<String, StudentData> allStudents = studentService.getAllStudents();
        
        if (allStudents.isEmpty()) {
            return;
        }
        
        //update course enrollments
        for (String course : teacherCourses) {
            List<String> enrolledStudents = teacherData.getStudentsInCourse(course);
            
            for (Map.Entry<String, StudentData> entry : allStudents.entrySet()) {
                String username = entry.getKey();
                StudentData student = entry.getValue();
                
                if (student.getEnrolledCourses().contains(course)) {
                    if (!enrolledStudents.contains(username)) {
                        teacherData.addStudentToCourse(course, username);
                    }
                }
            }
        }
    }
} 