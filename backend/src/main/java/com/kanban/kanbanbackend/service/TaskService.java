package com.kanban.kanbanbackend.service;
import com.kanban.kanbanbackend.dto.TaskUpdateRequest;
import com.kanban.kanbanbackend.dto.TaskRequest;
import com.kanban.kanbanbackend.entity.Board;
import com.kanban.kanbanbackend.entity.Task;
import com.kanban.kanbanbackend.repository.BoardRepository;
import com.kanban.kanbanbackend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kanban.kanbanbackend.entity.User;
import com.kanban.kanbanbackend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BoardRepository boardRepository;

    // Create Task
    public Task createTask(TaskRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Board board = boardRepository.findById(request.getBoardId()).orElse(null);

        if (board == null) {
            throw new RuntimeException("Board not found");
        }

        // 🔒 Board owner verification
        if (!board.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access Denied");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setBoard(board);
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        return taskRepository.save(task);
    }

    // Get All Tasks
    public List<Task> getAllTasks() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return List.of();
        }

        List<Board> boards = boardRepository.findByUser(user);

        List<Task> tasks = new java.util.ArrayList<>();

        for (Board board : boards) {
            tasks.addAll(taskRepository.findByBoard(board));
        }

        return tasks;
    }

    // Get Task By Id
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    // Update Task

    // Update Task
    public Task updateTask(Long id, TaskUpdateRequest request) {

        System.out.println("UPDATE API CALLED");

        Task existingTask = taskRepository.findById(id).orElse(null);

        if (existingTask == null) {
            return null;
        }

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setStatus(request.getStatus());
        existingTask.setPriority(request.getPriority());
        existingTask.setDueDate(request.getDueDate());

        return taskRepository.save(existingTask);
    }
    // Delete Task
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}