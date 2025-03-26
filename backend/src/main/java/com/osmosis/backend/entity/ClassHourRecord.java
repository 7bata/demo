package com.osmosis.backend.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 课时记录实体类
 */
@Data
public class ClassHourRecord {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private Integer teacherId;
    private LocalDate classDate;
    private String theme;
    private String content;
    private String status;  // completed, absent, canceled
    private String learningProgress;
    private String homework;
    private String nextClassGoal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 额外的非数据库字段，用于前端展示
    private String studentName;
    private String courseName;
    private String teacherName;
} 