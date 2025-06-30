package com.attendanceapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        
        switch (role) {
            case "ROLE_ADMIN":
                return "admin";
            case "ROLE_MANAGER":
                return "manager";
            case "ROLE_EMPLOYEE":
                return "employee";
            default:
                return "redirect:/login";
        }
    }
}
