package com.example.demo.controller;

import com.example.demo.service.TaskService;
import com.example.demo.dto.CreateTaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.dto.UpdateTaskRequest;
import com.example.demo.taskstatus.TaskStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//This class is a web controller whose methods can handle HTTP requests
//and whose return values can be written directly to the HTTP response.
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    //ApplicationContext
    //      │
    //      ├── creates TaskService
    //      │
    //      └── creates TaskController
    //               │
    //               └── injects TaskService
    // This is Dependency Injection

    //Spring
    // ↓
    //ApplicationContext
    // ↓
    //TaskService Bean
    // ↓
    //Constructor Injection
    // ↓
    //TaskController


    //Browser / Postman
    //       │
    //       │ GET /api/tasks
    //       ▼
    //Embedded Tomcat
    //       │
    //       ▼
    //Spring MVC
    //       │
    //       ▼
    //DispatcherServlet
    //       │
    //       ▼
    //Handler Mapping
    //       │
    //       │ finds:
    //       │ GET /api/tasks
    //       ▼
    //TaskController
    //       │
    //       ▼
    //getTasks()
    //       │
    //       ▼
    //"TaskFlow is running!"
    //       │
    //       ▼
    //HTTP Response
    @GetMapping("/me")
    public String me(Authentication authentication) {
        return authentication != null ? authentication.getName() : "Anonymous";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is public";
    }

    @GetMapping("/tasks")
    public List<TaskResponse> getTasks(
            @RequestParam(required = false) TaskStatus status
    ) {
        if (status == null) {
            return taskService.getTasks();
        }

        return taskService.getTasksByStatus(status);
    }

    @GetMapping("tasks/{id}")
    public TaskResponse getTask(@PathVariable Long id){
        return taskService.getTask(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(
            @Valid @RequestBody CreateTaskRequest request
    ){
        return taskService.createTask(request);
    }

    @PutMapping("tasks/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request
            ){
        return taskService.updateTask(id,request);
    }

    @DeleteMapping("tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @PathVariable Long id
    ){
        taskService.deleteTask(id);
    }
    @PatchMapping("tasks/{id}/complete")
    public TaskResponse completeTask(
            @PathVariable Long id
    ) {
        return taskService.completeTask(id);
    }
}




//Spring Boot
//     ↓
//SpringApplication.run()
//     ↓
//ApplicationContext
//     ↓
//Auto Configuration
//     ↓
//Component Scanning
//     ↓
//Beans
//     ↓
//Embedded Tomcat
//     ↓
//HTTP Request
//     ↓
//DispatcherServlet
//     ↓
//Controller
//     ↓
//HTTP Response