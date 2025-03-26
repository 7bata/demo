package com.osmosis.backend.entity;

/**
 * 课时统计数据实体类
 */
public class CourseHoursStats {
    private Integer remainingHours;  // 剩余课时
    private Integer consumedHours;   // 已消耗课时
    
    public CourseHoursStats() {
        this.remainingHours = 0;
        this.consumedHours = 0;
    }
    
    public CourseHoursStats(Integer remainingHours, Integer consumedHours) {
        this.remainingHours = remainingHours;
        this.consumedHours = consumedHours;
    }
    
    public Integer getRemainingHours() {
        return remainingHours;
    }
    
    public void setRemainingHours(Integer remainingHours) {
        this.remainingHours = remainingHours;
    }
    
    public Integer getConsumedHours() {
        return consumedHours;
    }
    
    public void setConsumedHours(Integer consumedHours) {
        this.consumedHours = consumedHours;
    }
    
    @Override
    public String toString() {
        return "CourseHoursStats{" +
                "remainingHours=" + remainingHours +
                ", consumedHours=" + consumedHours +
                '}';
    }
} 