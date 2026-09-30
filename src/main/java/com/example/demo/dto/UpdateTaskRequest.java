package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
        @NotBlank(message = "Title is required")
        @Size(
                min = 3,
                max = 100,
                message = "Title must be between 3 and 100 characters"
        )
        String title,

        @Size(
                max = 500,
                message = "Description cannot exceed 500 characters"
        )
        String description
) {
}
