package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
    @NotBlank(message = "Title is required")
    @Size(
            min=3,
            max=15,
            message = "Title must be between 3 and 15 characters"
    )
    String title,

    @Size(
            max = 500,
            message = "Description cannot exceed 500 characters"
    )
    String description
){
}
