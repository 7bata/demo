package com.osmosis.backend.entity;

import java.time.LocalDateTime;

/**
 * 课程实体类
 */
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
    
    // 添加课时相关字段
    private Integer totalHours;
    private Integer completedHours;
    
    // 标准getter/setter方法
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getTeacherName() {
        return teacherName;
    }
    
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    
    public Integer getTotalHours() {
        return totalHours;
    }
    
    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }
    
    public Integer getCompletedHours() {
        return completedHours;
    }
    
    public void setCompletedHours(Integer completedHours) {
        this.completedHours = completedHours;
    }
    
    // 用于MyBatis字段名不匹配的情况
    public void setTotal_hours(Integer hours) {
        this.totalHours = hours;
    }
    
    public void setCompleted_hours(Integer hours) {
        this.completedHours = hours;
    }
} 