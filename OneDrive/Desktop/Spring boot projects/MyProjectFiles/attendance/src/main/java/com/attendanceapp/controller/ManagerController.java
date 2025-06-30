package com.attendanceapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.attendanceapp.Repositories.AttendanceRepository;
import com.attendanceapp.Repositories.UserRepository;
import com.attendanceapp.model.Attendance;
import com.attendanceapp.model.User;

@Controller
public class ManagerController {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    public ManagerController(UserRepository userRepository, AttendanceRepository attendanceRepository) {
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/manager/dashboard")
    public String managerDashboard() {
        return "manager";
    }

    @GetMapping("/manager/mark-attendance")
    public String showMarkAttendanceForm(Model model, Authentication authentication) {
        System.out.println("Authenticated user: " + authentication.getName());
        System.out.println("Roles: " + authentication.getAuthorities());
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "mark-attendance";
    }


    @PostMapping("/manager/mark-attendance")
    public String markAttendance(@RequestParam(required = false) Long userId) {
        if (userId == null) {
            System.out.println("Error: userId is null.");
            return "redirect:/manager/mark-attendance?error=invalidUser";
        }
        System.out.println("Received request to mark attendance for user ID: " + userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));

        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<Attendance> userAttendances = attendanceRepository.findByUser(user);

        boolean alreadyCheckedIn = userAttendances.stream()
            .filter(a -> a.getCheckInTime() != null)
            .anyMatch(a -> a.getCheckInTime().isAfter(startOfDay) && a.getCheckInTime().isBefore(endOfDay));

        if (!alreadyCheckedIn) {
            Attendance attendance = new Attendance(user, LocalDateTime.now());
            attendanceRepository.save(attendance);
        } else {
            System.out.println("User has already checked in today.");
        }

        return "redirect:/manager/view-attendance";
    }


    @GetMapping("/manager/view-attendance")
    public String viewAttendance(Model model) {
        List<Attendance> attendanceRecords = attendanceRepository.findAll();
        attendanceRecords.removeIf(a -> a.getCheckInTime() == null); // filter out null check-ins
        model.addAttribute("attendanceRecords", attendanceRecords);
        return "view-attendance";
    }

}