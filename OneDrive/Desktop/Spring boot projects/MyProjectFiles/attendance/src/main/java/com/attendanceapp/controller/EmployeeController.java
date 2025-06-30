package com.attendanceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.attendanceapp.model.User;
import com.attendanceapp.service.AttendanceService;
import com.attendanceapp.service.UserService;

@Controller
public class EmployeeController {

    private final UserService userService;
    private final AttendanceService attendanceService;

    public EmployeeController(UserService userService, AttendanceService attendanceService) {
        this.userService = userService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/employee/dashboard")
    public String employeeDashboard() {
        return "employee";
    }

    @PostMapping("/employee/manual-check-in")
    public String manualCheckIn(@RequestParam String username, Model model) {
        User user = userService.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "User not found!");
            return "employee";
        }
        try {
            attendanceService.checkIn(user);
            model.addAttribute("success", "Checked in successfully!");
            return "check-in-success";
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "employee";
        }
    }

    @PostMapping("/employee/manual-check-out")
    public String manualCheckOut(@RequestParam String username, Model model) {
        User user = userService.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "User not found!");
            return "employee";
        }
        try {
            attendanceService.checkOut(user);
            model.addAttribute("success", "Checked out successfully!");
            return "check-out-success";
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "employee";
        }
    }
}
