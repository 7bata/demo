package com.osmosis.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private Date enrollDate;
    private Integer totalHours;
    private Integer completedHours;
    private String status; // 'active', 'completed', 'paused'
    
    // 关联数据
    private String studentName;
    private String courseName;
    private String teacherName;
} 