package com.osmosis.backend.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统通知实体类
 */
@Data
public class Notification {
    private Integer id;
    private String title;
    private String content;
    private Integer userId;  // 为空表示所有用户
    private Boolean isRead;
    private String type;  // system, course, attendance
    private LocalDateTime createdAt;
    
    // 额外的非数据库字段，用于前端展示
    private String senderName;
} 