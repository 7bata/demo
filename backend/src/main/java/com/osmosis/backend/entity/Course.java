package com.osmosis.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程实体类
 */
@Data
public class Course {
    private Integer id;
    private String courseName;
    private String courseCode;
    private String description;
    private Integer teacherId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 额外的非数据库字段，用于前端展示
    private String teacherName;
} 