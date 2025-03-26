package com.osmosis.backend.entity;

import lombok.Data;

import java.util.List;

/**
 * 课时历史记录响应实体类
 */
@Data
public class CourseHourHistoryResponse {
    private List<CourseHourHistory> records;
    private int total;
    
    public CourseHourHistoryResponse() {
    }
    
    public CourseHourHistoryResponse(List<CourseHourHistory> records, int total) {
        this.records = records;
        this.total = total;
    }
} 