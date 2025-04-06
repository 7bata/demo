package com.osmosis.backend.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 课时历史记录项实体类
 */
public class CourseHourHistory {
    private Integer id;
    private LocalDate date;
    private String formattedDate; // 添加格式化后的日期字符串
    private String subject;
    private String teacher;
    private Double hours;
    private String report;
    private String status;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean hasReport;
    
    public CourseHourHistory() {
    }
    
    public CourseHourHistory(Integer id, LocalDate date, String subject, String teacher, Double hours, String report) {
        this.id = id;
        this.setDate(date);
        this.subject = subject;
        this.teacher = teacher;
        this.hours = hours;
        this.report = report;
    }
    
    // Getter和Setter方法
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
        if (date != null) {
            this.formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }
    
    public String getFormattedDate() {
        return formattedDate;
    }
    
    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
        if (formattedDate != null && !formattedDate.isEmpty()) {
            try {
                this.date = LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                // 解析失败，忽略
            }
        }
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getTeacher() {
        return teacher;
    }
    
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    
    public Double getHours() {
        return hours;
    }
    
    public void setHours(Double hours) {
        this.hours = hours;
    }
    
    public String getReport() {
        return report;
    }
    
    public void setReport(String report) {
        this.report = report;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public Boolean getHasReport() {
        return hasReport;
    }
    
    public void setHasReport(Boolean hasReport) {
        this.hasReport = hasReport;
    }
} 