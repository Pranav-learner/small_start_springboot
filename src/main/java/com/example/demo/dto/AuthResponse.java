package com.example.demo.dto;

public record AuthResponse(
        String token,
        String type,
        String email,
        String name,
        String role
) {
    public AuthResponse(String token, String email, String name, String role) {
        this(token, "Bearer", email, name, role);
    }
}
