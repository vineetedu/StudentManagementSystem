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
        
        //assign courses to teacher
        List<String> availableCourses = courseCatalogService.getAvailableCourses();
        for (String course : availableCourses) {
            teacher.addCourse(course);
        }
        
        teachers.put("teacher", teacher);
        
        //sync with student enrollments
        Map<String, StudentData> allStudents = studentDataService.getAllStudents();
        for (Map.Entry<String, StudentData> entry : allStudents.entrySet()) {
            String studentUsername = entry.getKey();
            StudentData student = entry.getValue();
            
            for (String course : student.getEnrolledCourses()) {
                if (availableCourses.contains(course)) {
                    teacher.addStudentToCourse(course, studentUsername);
                }
            }
        }
    }

    //retrieve teacher's academic records
    public TeacherData getTeacherData(String username) {
        TeacherData teacher = teachers.get(username);
        if (teacher == null) {
            initializeTeacherData();
            teacher = teachers.get(username);
        }
        return teacher;
    }

    //update teacher's academic records
    public void updateTeacherData(TeacherData teacherData) {
        if (teacherData != null) {
            teachers.put(teacherData.getUsername(), teacherData);
        }
    }

    //enroll a student in one of teacher's courses
    public void addStudentToCourse(String teacherUsername, String course, String studentUsername) {
        TeacherData teacher = teachers.get(teacherUsername);
        if (teacher != null) {
            teacher.addStudentToCourse(course, studentUsername);
        }
    }

    //record a student's grade for an assignment
    public void updateStudentGrade(String teacherUsername, String course, String studentUsername, String assignmentTitle, double grade) {
        if (grade < 0 || grade > 100) {
            return;
        }
        
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

    //record student attendance for today
    public void markStudentAttendance(String teacherUsername, String course, String studentUsername, boolean present) {
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
    
    //record student attendance for a specific date
    public void markStudentAttendanceWithDate(String teacherUsername, String course, String studentUsername, String date, boolean present) {
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
    
    //get list of students enrolled in a course
    public List<String> getStudentsInCourse(String course) {
        TeacherData teacher = teachers.get("teacher");
        if (teacher != null) {
            return teacher.getStudentsInCourse(course);
        }
        return new ArrayList<>();
    }
    
    //get list of all teacher usernames
    public List<String> getAllTeacherUsernames() {
        return new ArrayList<>(teachers.keySet());
    }
    
    //assign a course to a teacher
    public void assignCourse(String teacherUsername, String course) {
        TeacherData teacher = teachers.get(teacherUsername);
        if (teacher != null && courseCatalogService.getAvailableCourses().contains(course)) {
            teacher.addCourse(course);
            updateTeacherData(teacher);
        }
    }
    
    //remove a course from a teacher
    public void removeCourse(String teacherUsername, String course) {
        TeacherData teacher = teachers.get(teacherUsername);
        if (teacher != null) {
            teacher.removeCourse(course);
            updateTeacherData(teacher);
        }
    }
} 