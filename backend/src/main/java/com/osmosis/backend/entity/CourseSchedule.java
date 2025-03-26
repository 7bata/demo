package com.osmosis.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 课程安排实体类
 */
@Data
public class CourseSchedule {
    private Integer id;
    private Integer courseId;
    private Integer weekday;  // 1-7 表示周一至周日
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 额外的非数据库字段，用于前端展示
    private String courseName;
    private String courseCode;
    private String teacherName;
} 