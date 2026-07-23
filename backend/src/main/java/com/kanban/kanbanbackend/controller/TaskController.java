package com.kanban.kanbanbackend.controller;
import com.kanban.kanbanbackend.dto.TaskRequest;
import com.kanban.kanbanbackend.dto.TaskUpdateRequest;
import com.kanban.kanbanbackend.entity.Task;
import com.kanban.kanbanbackend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Create Task
    @PostMapping
    public Task createTask(@RequestBody TaskRequest request)  {System.out.println("Controller reached");
        System.out.println("Title = " + request.getTitle());
        System.out.println("Description = " + request.getDescription());
        System.out.println("Status = " + request.getStatus());
        System.out.println("BoardId = " + request.getBoardId());
        System.out.println("Priority = " + request.getPriority());
        System.out.println("DueDate = " + request.getDueDate());
        return taskService.createTask(request);
    }

    // Get All Tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    // Get Task By Id
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    // Update Task
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id,
                           @RequestBody TaskUpdateRequest request) {
        System.out.println("PUT CONTROLLER HIT");

        return taskService.updateTask(id, request);
    }

    // Delete Task
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "Task deleted successfully";
    }
}