package com.osmosis.backend.entity;

import java.util.Date;

/**
 * 学生选课实体类
 */
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
    
    // 默认构造函数
    public StudentCourse() {
    }
    
    // 全参数构造函数
    public StudentCourse(Integer id, Integer studentId, Integer courseId, Date enrollDate,
                         Integer totalHours, Integer completedHours, String status,
                         String studentName, String courseName, String teacherName) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollDate = enrollDate;
        this.totalHours = totalHours;
        this.completedHours = completedHours;
        this.status = status;
        this.studentName = studentName;
        this.courseName = courseName;
        this.teacherName = teacherName;
    }
    
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
    
    public Date getEnrollDate() {
        return enrollDate;
    }
    
    public void setEnrollDate(Date enrollDate) {
        this.enrollDate = enrollDate;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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