package com.osmosis.backend.service;

import com.osmosis.backend.entity.*;

import java.util.List;

/**
 * 学生服务接口
 */
public interface StudentService {
    
    /**
     * 获取学生的课程列表
     */
    List<Course> getStudentCourses(Integer studentId);
    
    /**
     * 获取学生的课程表
     */
    List<CourseSchedule> getStudentSchedule(Integer studentId);
    
    /**
     * 获取学生的课程详情
     */
    Course getCourseDetail(Integer courseId);
    
    /**
     * 获取课程的安排
     */
    List<CourseSchedule> getCourseSchedules(Integer courseId);
    
    /**
     * 获取学生的课时记录
     */
    List<ClassHourRecord> getStudentClassHourRecords(Integer studentId);
    
    /**
     * 获取学生特定课程的课时记录
     */
    List<ClassHourRecord> getStudentCourseClassHourRecords(Integer studentId, Integer courseId);
    
    /**
     * 获取学生的考勤记录
     */
    List<StudentAttendance> getStudentAttendance(Integer studentId);
    
    /**
     * 获取学生特定课程的考勤记录
     */
    List<StudentAttendance> getStudentCourseAttendance(Integer studentId, Integer courseId);
    
    /**
     * 获取课程的学习资料
     */
    List<CourseMaterial> getCourseMaterials(Integer courseId);
    
    /**
     * 获取学生的所有通知
     */
    List<Notification> getStudentNotifications(Integer studentId);
    
    /**
     * 获取学生的未读通知
     */
    List<Notification> getStudentUnreadNotifications(Integer studentId);
    
    /**
     * 将通知标记为已读
     */
    boolean markNotificationAsRead(Integer notificationId, Integer studentId);
    
    /**
     * 获取学生的课时统计
     */
    CourseHoursStats getStudentCourseHoursStats(Integer studentId);
    
    /**
     * 获取学生的课时历史记录
     */
    CourseHourHistoryResponse getStudentCourseHoursHistory(Integer studentId);
} 