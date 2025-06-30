package com.attendanceapp.Repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.attendanceapp.model.Attendance;
import com.attendanceapp.model.User;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Find all attendance records for a specific user
    List<Attendance> findByUser(User user);

    // Find attendance records for a specific date (based on check-in time's date)
    @Query("SELECT a FROM Attendance a WHERE DATE(a.checkInTime) = :date")
    List<Attendance> findByDate(@Param("date") LocalDate date);

    // Find attendance records for a user on a specific date
    @Query("SELECT a FROM Attendance a WHERE a.user = :user AND DATE(a.checkInTime) = :date")
    List<Attendance> findByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);

    // Find all users who checked in on a particular date (for attendance summary)
    @Query("SELECT a.user FROM Attendance a WHERE DATE(a.checkInTime) = :date")
    List<User> findUsersByDate(@Param("date") LocalDate date);

    // Count present users for a particular day (by checking who checked in)
    @Query("SELECT COUNT(a) FROM Attendance a WHERE DATE(a.checkInTime) = :date")
    long countByDate(@Param("date") LocalDate date);

    // Find all records between check-in and check-out timestamps (for reports)
    List<Attendance> findByCheckInTimeBetween(LocalDateTime start, LocalDateTime end);
}
