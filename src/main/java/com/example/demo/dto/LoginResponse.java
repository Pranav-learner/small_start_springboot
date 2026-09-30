package com.example.demo.dto;

public record LoginResponse(
        String token,
        String type
) {
    public LoginResponse(String token) {
        this(token, "Bearer");
    }
}