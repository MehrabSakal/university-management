
package com.example.university_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/admins")
    public String admins() {
        return "admins";
    }

    @GetMapping("/teachers")
    public String teachers() {
        return "teachers";
    }

    @GetMapping("/students")
    public String students() {
        return "students";
    }

    @GetMapping("/edit/{resource}/{userId}")
    public String editUserPage(
            @PathVariable String resource,
            @PathVariable Long userId,
            Model model) {

        if (!resource.equals("admins")
                && !resource.equals("teachers")
                && !resource.equals("students")) {
            return "redirect:/dashboard";
        }

        model.addAttribute("resource", resource);
        model.addAttribute("userId", userId);

        return "edit-user";
    }
}
