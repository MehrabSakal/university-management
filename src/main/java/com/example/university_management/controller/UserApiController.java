package com.example.university_management.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.university_management.model.UniversityUser;
import com.example.university_management.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserApiController(
            UserRepository repository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    private String getRole(String resource) {
        return switch (resource) {
            case "admins" -> "ADMIN";
            case "teachers" -> "TEACHER";
            case "students" -> "STUDENT";
            default -> null;
        };
    }

    // READ: Get all users of a resource type
    @GetMapping("/{resource:admins|teachers|students}")
    public ResponseEntity<?> getAll(
            @PathVariable String resource) {

        String role = getRole(resource);

        if (role == null) {
            return ResponseEntity.badRequest().body("Invalid resource");
        }

        return ResponseEntity.ok(repository.findByRole(role));
    }

    // READ: Get one user
    @GetMapping("/{resource:admins|teachers|students}/{id}")
    public ResponseEntity<?> getOne(
            @PathVariable String resource,
            @PathVariable Long id) {

        String role = getRole(resource);

        Optional<UniversityUser> user = repository.findById(id);

        if (user.isEmpty() || !user.get().getRole().equals(role)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user.get());
    }

    // CREATE: Add a user
    @PostMapping("/{resource:admins|teachers|students}")
    public ResponseEntity<?> create(
            @PathVariable String resource,
            @RequestBody UniversityUser user) {

        String role = getRole(resource);

        if (role == null) {
            return ResponseEntity.badRequest().body("Invalid resource");
        }

        if (user.getName() == null || user.getName().isBlank()
                || user.getEmail() == null || user.getEmail().isBlank()
                || user.getPassword() == null
                || user.getPassword().length() < 6) {
            return ResponseEntity.badRequest()
                    .body("Name, email, and a password of at least 6 characters are required.");
        }

        if (repository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists.");
        }

        user.setId(null);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(repository.save(user));
    }

    // UPDATE: Change name, department, or password
    @PutMapping("/{resource:admins|teachers|students}/{id}")
    public ResponseEntity<?> update(
            @PathVariable String resource,
            @PathVariable Long id,
            @RequestBody Map<String, String> data) {

        String role = getRole(resource);

        Optional<UniversityUser> result = repository.findById(id);

        if (result.isEmpty()
                || !result.get().getRole().equals(role)) {
            return ResponseEntity.notFound().build();
        }

        UniversityUser user = result.get();

        if (data.containsKey("name")) {
            if (data.get("name") == null || data.get("name").isBlank()) {
                return ResponseEntity.badRequest().body("Name is required.");
            }
            user.setName(data.get("name"));
        }

        if (data.containsKey("department")) {
            user.setDepartment(data.get("department"));
        }

        if (data.containsKey("password")
                && data.get("password") != null
                && !data.get("password").isBlank()) {

            if (data.get("password").length() < 6) {
                return ResponseEntity.badRequest()
                        .body("Password must be at least 6 characters.");
            }

            user.setPassword(
                    passwordEncoder.encode(data.get("password")));
        }

        return ResponseEntity.ok(repository.save(user));
    }

    // DELETE: Remove a user
    @DeleteMapping("/{resource:admins|teachers|students}/{id}")
    public ResponseEntity<?> delete(
            @PathVariable String resource,
            @PathVariable Long id) {

        String role = getRole(resource);

        Optional<UniversityUser> result = repository.findById(id);

        if (result.isEmpty()
                || !result.get().getRole().equals(role)) {
            return ResponseEntity.notFound().build();
        }

        repository.delete(result.get());

        return ResponseEntity.noContent().build();
    }
}