package com.kanban.kanbanbackend.controller;
import com.kanban.kanbanbackend.entity.User;
import com.kanban.kanbanbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.kanban.kanbanbackend.dto.LoginRequest;
import org.springframework.security.core.Authentication;
import com.kanban.kanbanbackend.dto.ProfileResponse;
import com.kanban.kanbanbackend.dto.ChangePasswordRequest;
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                           @RequestBody User user) {

        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest request) {

        return userService.loginUser(
                request.getEmail(),
                request.getPassword()
        );
    }

    @GetMapping("/profile")
    public User getProfile(Authentication authentication) {

        String email = authentication.getName();

        return userService.getUserByEmail(email);
    }

    @PutMapping("/profile")
    public User updateProfile(Authentication authentication,
                              @RequestBody User user) {

        String email = authentication.getName();

        return userService.updateProfile(email, user);
    }
    @PostMapping("/change-password")
    public String changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request) {

        return userService.changePassword(
                authentication.getName(),
                request
        );
    }

}