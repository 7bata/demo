package com.osmosis.backend.entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 课时历史记录项实体类
 */
public class CourseHourHistory {
    private Integer id;
    private LocalDate date;
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
        this.date = date;
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