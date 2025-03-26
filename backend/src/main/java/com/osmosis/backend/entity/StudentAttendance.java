package com.osmosis.backend.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生考勤实体类
 */
@Data
public class StudentAttendance {
    private Integer id;
    private Integer studentId;
    private Integer courseScheduleId;
    private LocalDate attendanceDate;
    private String status;  // present, late, absent, excused
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 额外的非数据库字段，用于前端展示
    private String studentName;
    private String courseName;
    private String courseCode;
    private Integer weekday;
    private String location;
} 