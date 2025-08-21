package com.example.learning.dto;

public class AuthDTOs {
    public static class LoginRequest {
        public String username;
        public String password;
    }
    public static class RegisterRequest {
        public String username;
        public String password;
        public String fullName;
        public String role; // optional: ADMIN/TEACHER/STUDENT; only admin can assign role other than STUDENT
    }
    public static class AuthResponse {
        public String token;
        public AuthResponse(String token){ this.token = token; }
    }
}
