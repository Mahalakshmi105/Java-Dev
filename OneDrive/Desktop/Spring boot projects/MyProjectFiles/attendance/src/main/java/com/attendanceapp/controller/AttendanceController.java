package com.attendanceapp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.attendanceapp.Repositories.UserRepository;
import com.attendanceapp.model.User;
import com.attendanceapp.service.AttendanceService;

@Controller
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    public AttendanceController(AttendanceService attendanceService, UserRepository userRepository) {
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
    }

    @PostMapping("/employee/check-in")
    public String checkIn(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            model.addAttribute("error", "Authenticated user not found.");
            return "error";
        }
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElse(null);
        if (user == null) {
            model.addAttribute("error", "User not found in database.");
            return "error";
        }
        try {
            attendanceService.checkIn(user);
            model.addAttribute("success", "Checked in successfully!");
            model.addAttribute("attendanceHistory", attendanceService.getAttendanceHistory(user));
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "employee-attendance";
    }

    @PostMapping("/employee/check-out")
    public String checkOut(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            model.addAttribute("error", "Authenticated user not found.");
            return "error";
        }
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElse(null);
        if (user == null) {
            model.addAttribute("error", "User not found in database.");
            return "error";
        }
        try {
            attendanceService.checkOut(user);
            model.addAttribute("success", "Checked out successfully!");
            model.addAttribute("attendanceHistory", attendanceService.getAttendanceHistory(user));
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "employee-attendance";
    }

    @GetMapping("/employee/attendance")
    public String viewAttendanceHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            model.addAttribute("error", "Authenticated user not found.");
            return "employee";
        }
        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            model.addAttribute("error", "User not found in database.");
            return "employee";
        }
        model.addAttribute("attendanceHistory", attendanceService.getAttendanceHistory(user));
        return "employee-attendance";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        e.printStackTrace();
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}
