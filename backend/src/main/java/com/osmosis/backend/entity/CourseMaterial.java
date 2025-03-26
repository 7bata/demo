package com.osmosis.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程资料实体类
 */
@Data
public class CourseMaterial {
    private Integer id;
    private Integer courseId;
    private String title;
    private String description;
    private String filePath;
    private String fileType;
    private Integer uploadBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 额外的非数据库字段，用于前端展示
    private String courseName;
    private String uploaderName;
} 