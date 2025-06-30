package com.attendanceapp.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.attendanceapp.Repositories.AttendanceRepository;
import com.attendanceapp.model.Attendance;
import com.attendanceapp.model.User;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Transactional
    public void checkIn(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User cannot be null and must have a valid ID during check-in");
        }
        System.out.println("Checking in user: " + user.getUsername());

        // Ensure user hasn't already checked in without checking out
        List<Attendance> attendances = attendanceRepository.findByUser(user);
        boolean hasActiveCheckIn = attendances.stream().anyMatch(a -> a.getCheckOutTime() == null);

        if (hasActiveCheckIn) {
            throw new IllegalStateException("User has already checked in and not checked out yet.");
        }

        // Ensure user is correctly associated
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setCheckInTime(LocalDateTime.now());
        attendanceRepository.save(attendance);
    }

    @Transactional
    public void checkOut(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null during check-out");
        }
        System.out.println("Checking out user: " + user.getUsername());

        List<Attendance> attendances = attendanceRepository.findByUser(user);
        Attendance lastAttendance = attendances.stream()
            .filter(a -> a.getCheckOutTime() == null)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No active check-in found for user."));

        lastAttendance.setCheckOutTime(LocalDateTime.now());
        attendanceRepository.save(lastAttendance);
    }

    public List<Attendance> getAttendanceHistory(User user) {
        return attendanceRepository.findByUser(user);
    }
}
