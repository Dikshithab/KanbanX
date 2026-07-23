package com.kanban.kanbanbackend.service;
import com.kanban.kanbanbackend.entity.User;
import com.kanban.kanbanbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.kanban.kanbanbackend.dto.ProfileResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.kanban.kanbanbackend.dto.ChangePasswordRequest;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User updateUser(Long id, User updatedUser) {

        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());

            return userRepository.save(existingUser);
        }

        return null;
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Autowired
    private JwtService jwtService;

    public String loginUser(String email, String password) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            System.out.println("User not found");
            return "Invalid Credentials";
        }

        System.out.println("Entered Password: " + password);
        System.out.println("DB Hash: " + user.getPassword());

        boolean match = passwordEncoder.matches(password, user.getPassword());

        System.out.println("Password Match = " + match);

        if (!match) {
            return "Invalid Credentials";
        }

        return jwtService.generateToken(user.getEmail());
    }
    public ProfileResponse getProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return null;
        }

        return new ProfileResponse(
                user.getName(),
                user.getEmail()
        );
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User updateProfile(String email, User updatedUser) {
        System.out.println("Logged in Email = " + email);
        System.out.println("New Name = " + updatedUser.getName());
        System.out.println("New Email = " + updatedUser.getEmail());

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            System.out.println("User Not Found");
            return null;
        }
        // Check if the new email is already used by another user
        if (!user.getEmail().equals(updatedUser.getEmail())) {

            if (userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }

        }
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());

        return userRepository.save(user);
    }
    public String changePassword(
            String email,
            ChangePasswordRequest request) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null)
            return "User Not Found";

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            return "Old Password Incorrect";
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        return "Password Updated Successfully";
    }
}
