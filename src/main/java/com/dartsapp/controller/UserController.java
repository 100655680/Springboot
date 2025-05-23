package com.dartsapp.controller;

import com.dartsapp.model.User;
import com.dartsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setMainStatus(userDetails.getMainStatus());
        user.setSubStatus(userDetails.getSubStatus());
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        userRepository.delete(user);
        return "User deleted successfully!";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());
        if (!optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "User not found"));
        }

        User user = optionalUser.get();
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Invalid password"));
        }

        user.setMainStatus("online");
        user.setSubStatus("away");
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "User already exists"));
        }
        newUser.setMainStatus("offline");
        newUser.setSubStatus(null);
        userRepository.save(newUser);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "User not found"));
        }
        User user = optionalUser.get();
        user.setMainStatus("offline");
        user.setSubStatus(null);
        userRepository.save(user);
        return ResponseEntity.ok(Collections.singletonMap("message", "Logged out successfully"));
    }

    // Updated endpoint: can update mainStatus and/or subStatus
    @PostMapping("/status")
    public ResponseEntity<?> updateStatus(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "User not found"));
        }
        User user = optionalUser.get();

        if (payload.containsKey("mainStatus")) {
            user.setMainStatus(payload.get("mainStatus"));
        }
        if (payload.containsKey("subStatus")) {
            user.setSubStatus(payload.get("subStatus"));
        }

        userRepository.save(user);
        return ResponseEntity.ok(Collections.singletonMap("message", "Status updated"));
    }
}
