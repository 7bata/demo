package com.osmosis.backend.controller;

import com.osmosis.backend.entity.*;
import com.osmosis.backend.service.TeacherService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    
    @Resource
    private TeacherService teacherService;
    
    /**
     * 获取教师的课程列表
     */
    @GetMapping("/courses")
    public RestBean<List<StudentCourse>> getTeacherCourses(@RequestParam Integer teacherId) {
        try {
            List<StudentCourse> courses = teacherService.getTeacherCourses(teacherId);
            return RestBean.success(courses);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.error(500, "获取课程列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取教师的课程表
     */
    @GetMapping("/course-schedule")
    public RestBean<List<CourseSchedule>> getTeacherSchedule(@RequestParam Integer teacherId) {
        try {
            List<CourseSchedule> schedules = teacherService.getTeacherSchedule(teacherId);
            return RestBean.success(schedules);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.error(500, "获取课程表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取课程的学生列表
     */
    @GetMapping("/course-students/{courseId}")
    public RestBean<List<StudentCourse>> getCourseStudents(@PathVariable Integer courseId) {
        try {
            List<StudentCourse> students = teacherService.getCourseStudents(courseId);
            return RestBean.success(students);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.error(500, "获取学生列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取学生特定课程的课时记录
     */
    @GetMapping("/student-course-records")
    public RestBean<List<ClassHourRecord>> getStudentCourseRecords(
            @RequestParam Integer studentId, 
            @RequestParam Integer courseId) {
        try {
            List<ClassHourRecord> records = teacherService.getStudentCourseClassHourRecords(studentId, courseId);
            return RestBean.success(records);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.error(500, "获取课时记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取课程详情
     */
    @GetMapping("/course/{courseId}")
    public RestBean<Course> getCourseDetail(@PathVariable Integer courseId) {
        try {
            Course course = teacherService.getCourseDetail(courseId);
            return RestBean.success(course);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.error(500, "获取课程详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 保存课时记录
     */
    @PostMapping("/class-hour-record")
    public RestBean<Boolean> saveClassHourRecord(@RequestBody ClassHourRecord record) {
        try {
            boolean result = teacherService.saveClassHourRecord(record);
            return RestBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.error(500, "保存课时记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新课时记录
     */
    @PutMapping("/class-hour-record")
    public RestBean<Boolean> updateClassHourRecord(@RequestBody ClassHourRecord record) {
        try {
            boolean result = teacherService.updateClassHourRecord(record);
            return RestBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.error(500, "更新课时记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除课时记录
     */
    @DeleteMapping("/class-hour-record/{recordId}")
    public RestBean<Boolean> deleteClassHourRecord(@PathVariable Integer recordId) {
        try {
            boolean result = teacherService.deleteClassHourRecord(recordId);
            return RestBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.error(500, "删除课时记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取课时记录详情
     */
    @GetMapping("/class-hour-record/{recordId}")
    public RestBean<ClassHourRecord> getClassHourRecord(@PathVariable Integer recordId) {
        try {
            ClassHourRecord record = teacherService.getClassHourRecordById(recordId);
            return RestBean.success(record);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.error(500, "获取课时记录详情失败: " + e.getMessage());
        }
    }
} 