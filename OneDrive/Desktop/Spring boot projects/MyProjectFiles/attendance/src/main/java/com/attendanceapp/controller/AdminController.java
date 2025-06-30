package com.attendanceapp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.attendanceapp.Repositories.UserRepository;
import com.attendanceapp.model.User;
import com.attendanceapp.model.User.Role;

@Controller
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin";
    }

    @GetMapping("/admin/add-user")
    public String showAddUserForm() {
        return "add-user";
    }

    @PostMapping("/admin/add-user")
    public String addUser(@RequestParam String username, 
                          @RequestParam String password,
                          @RequestParam Role role) {
        // Create and save user
        User user = User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
        userRepository.save(user);
        return "redirect:/admin/view-users";
    }

    @GetMapping("/admin/view-users")
    public String viewUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "view-users";
    }
}
