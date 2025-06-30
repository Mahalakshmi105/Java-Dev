package com.attendanceapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    // Constructors, getters, and setters
    public Attendance() {}

    public Attendance(User user, LocalDateTime checkInTime) {
        this.user = user;
        this.checkInTime = checkInTime;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }
    public void setUser(User user) {
        this.user = user;
    }
    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }
}
