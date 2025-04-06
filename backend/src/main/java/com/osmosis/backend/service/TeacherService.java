package com.osmosis.backend.service;

import com.osmosis.backend.entity.*;

import java.util.List;

/**
 * 教师服务接口
 */
public interface TeacherService {
    
    /**
     * 获取教师的课程列表
     */
    List<StudentCourse> getTeacherCourses(Integer teacherId);
    
    /**
     * 获取教师的课程表
     */
    List<CourseSchedule> getTeacherSchedule(Integer teacherId);
    
    /**
     * 获取课程的学生列表
     */
    List<StudentCourse> getCourseStudents(Integer courseId);
    
    /**
     * 获取学生特定课程的课时记录
     */
    List<ClassHourRecord> getStudentCourseClassHourRecords(Integer studentId, Integer courseId);
    
    /**
     * 获取课程详情
     */
    Course getCourseDetail(Integer courseId);
    
    /**
     * 保存课时记录
     */
    boolean saveClassHourRecord(ClassHourRecord record);
    
    /**
     * 更新课时记录
     */
    boolean updateClassHourRecord(ClassHourRecord record);
    
    /**
     * 删除课时记录
     */
    boolean deleteClassHourRecord(Integer recordId);
    
    /**
     * 通过ID获取课时记录
     */
    ClassHourRecord getClassHourRecordById(Integer recordId);
} 