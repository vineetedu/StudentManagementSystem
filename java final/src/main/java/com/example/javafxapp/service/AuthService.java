package com.example.javafxapp.service;

//handles user authentication and authorization
public class AuthService {
    //predefined user credentials for demo purposes
    private static final String ADMIN_USERNAME = "admin";
    private static final String TEACHER_USERNAME = "teacher";
    private static final String[] STUDENT_USERNAMES = {"student1", "student2", "student3"};
    private static final String VALID_PASSWORD = "password";

    //verify if provided credentials are valid for the given user type
    public static boolean validateCredentials(String userType, String username, String password) {
        //reject empty credentials
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }

        //check credentials based on user type
        switch(userType) {
            case "Admin":
                return username.equals(ADMIN_USERNAME) && password.equals(VALID_PASSWORD);
            case "Teacher":
                return username.equals(TEACHER_USERNAME) && password.equals(VALID_PASSWORD);
            case "Student":
                //check password first for efficiency
                if (!password.equals(VALID_PASSWORD)) {
                    return false;
                }
                //check if username is in the list of valid student usernames
                for (String validUsername : STUDENT_USERNAMES) {
                    if (username.equals(validUsername)) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }
    
    //get list of valid usernames for a given user type
    public static String[] getValidUsernames(String userType) {
        switch(userType) {
            case "Admin":
                return new String[]{ADMIN_USERNAME};
            case "Teacher":
                return new String[]{TEACHER_USERNAME};
            case "Student":
                return STUDENT_USERNAMES;
            default:
                return new String[0];
        }
    }
} 