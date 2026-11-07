package com.example.university_management.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.university_management.model.UniversityUser;
import com.example.university_management.repository.UserRepository;

@Controller
public class SetupController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public SetupController(
            UserRepository repository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/setup")
    public String setup() {
        if (repository.countByRole("ADMIN") > 0) {
            return "redirect:/login";
        }

        return "setup";
    }

    @PostMapping("/setup")
    public String createAdmin(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String department,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (repository.countByRole("ADMIN") > 0) {
            return "redirect:/login";
        }

        if (name.isBlank() || email.isBlank()
                || password.length() < 6) {
            model.addAttribute(
                    "error",
                    "Enter all required fields. Password must be at least 6 characters.");
            return "setup";
        }

        if (repository.existsByEmail(email)) {
            model.addAttribute("error", "Email already exists.");
            return "setup";
        }

        UniversityUser admin = new UniversityUser();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setDepartment(department);
        admin.setRole("ADMIN");

        repository.save(admin);

        redirectAttributes.addFlashAttribute(
                "message",
                "Admin created. Please log in.");

        return "redirect:/login";
    }
}