package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.dto.CreateTaskRequest;
import com.example.demo.dto.TaskResponse;
import com.example.demo.dto.UpdateTaskRequest;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.repository.TaskRepository;
import com.example.demo.taskstatus.TaskStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
//This tells Spring that TaskService should be discovered and managed as a Spring Bean.
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


//GET /api/tasks
//       ↓
//Controller
//       ↓
//Service
//       ↓
//taskRepository.findAll()
//       ↓
//Hibernate
//       ↓
//SQL
//       ↓
//PostgreSQL
//       ↓
//List<Task>
//       ↓
//TaskResponse
//       ↓
//JSON
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasks(){
        return taskRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }
    @Transactional(readOnly = true)
    public TaskResponse getTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return toResponse(task);
    }

    @Transactional
    public TaskResponse createTask(CreateTaskRequest request){

        Task task = toEntity(request);
        Task savedtask = taskRepository.save(task);
        return  toResponse(savedtask);
    }
    @Transactional
    public TaskResponse updateTask(
            Long id,
            UpdateTaskRequest request
    ){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setUpdatedAt(LocalDateTime.now());

        //@Transactional
        //      ↓
        //Persistence Context created
        //      ↓
        //findById(id)
        //      ↓
        //Task loaded from DB
        //      ↓
        //Task becomes MANAGED
        //      ↓
        //task.setTitle(...)
        //      ↓
        //Persistence Context now has modified entity
        //      ↓
        //Transaction commits
        //      ↓
        //Hibernate Dirty Checking
        //      ↓
        //Hibernate generates UPDATE
        //      ↓
        //PostgreSQL
//        Task savedtask = taskRepository.save(task);
        return toResponse(task);
    }

    @Transactional
    public void deleteTask(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByStatus(
            TaskStatus status
    ) {

        return taskRepository.findByStatus(status)
                .stream()
                .map(this :: toResponse)
                .toList();
    }

    @Transactional
    public TaskResponse completeTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new TaskNotFoundException(id));

        task.setStatus(TaskStatus.COMPLETED);
        task.setUpdatedAt(LocalDateTime.now());

        return toResponse(task);
    }

    private TaskResponse toResponse(Task task){
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    private Task toEntity(CreateTaskRequest request) {
        return new Task(
                null,
                request.title(),
                request.description(),
                TaskStatus.TODO
        );
    }
}
//What is a Bean?
//A Bean is simply an object whose lifecycle is managed by Spring's IoC container.
//@Service
//public class TaskService {
//    ...
//}
//causes Spring to discover TaskService and register an instance of it as a Bean.
//Likewise:
//@RestController
//public class TaskController {
//    ...
//}
//causes Spring to manage the controller as a Bean.



//What is IoC?
//Normally, you might write:
//TaskService taskService = new TaskService();
//TaskController controller = new TaskController(taskService);
//Your code controls object creations
//That's traditional control.
//Spring reverses that:
//You write classes
//       ↓
//Spring discovers them
//       ↓
//Spring creates objects
//       ↓
//Spring connects dependencies
//       ↓
//Spring manages their lifecycle
//That's Inversion of Control.

// IoC = control over object creation and management is transferred from your application code to the Spring container.