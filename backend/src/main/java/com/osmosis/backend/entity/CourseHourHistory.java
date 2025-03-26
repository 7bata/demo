package com.osmosis.backend.entity;

import lombok.Data;

import java.time.LocalDate;

/**
 * 课时历史记录项实体类
 */
@Data
public class CourseHourHistory {
    private Integer id;
    private LocalDate date;
    private String subject;
    private String teacher;
    private Double hours;
    private String report;
    
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
} 