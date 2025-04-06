package com.osmosis.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 课时记录实体类
 */
public class ClassHourRecord {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private Integer teacherId;
    private LocalDate classDate;
    private String startTime;  // 上课开始时间 - 新增字段
    private String endTime;    // 上课结束时间 - 新增字段
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
    
    // Getter和Setter方法
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
    
    public Integer getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
    
    public Integer getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
    
    public LocalDate getClassDate() {
        return classDate;
    }
    
    public void setClassDate(LocalDate classDate) {
        this.classDate = classDate;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }
    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public String getTheme() {
        return theme;
    }
    
    public void setTheme(String theme) {
        this.theme = theme;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getLearningProgress() {
        return learningProgress;
    }
    
    public void setLearningProgress(String learningProgress) {
        this.learningProgress = learningProgress;
    }
    
    public String getHomework() {
        return homework;
    }
    
    public void setHomework(String homework) {
        this.homework = homework;
    }
    
    public String getNextClassGoal() {
        return nextClassGoal;
    }
    
    public void setNextClassGoal(String nextClassGoal) {
        this.nextClassGoal = nextClassGoal;
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
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getTeacherName() {
        return teacherName;
    }
    
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
} 