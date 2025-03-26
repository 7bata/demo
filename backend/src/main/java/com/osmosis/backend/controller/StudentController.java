package com.osmosis.backend.controller;

import com.osmosis.backend.entity.*;
import com.osmosis.backend.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    
    @Resource
    private StudentService studentService;
    
    /**
     * 获取学生课程表
     */
    @PostMapping("/course-schedule")
    public RestBean<List<CourseSchedule>> getCourseSchedule(@RequestParam Integer studentId) {
        try {
            System.out.println("获取学生课程表: studentId=" + studentId);
            List<CourseSchedule> schedules = studentService.getStudentSchedule(studentId);
            return RestBean.success(schedules);
        } catch (Exception e) {
            System.err.println("获取课程表失败: " + e.getMessage());
            e.printStackTrace();
            return RestBean.error(500, "获取课程表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取学生课时统计
     */
    @PostMapping("/course-hours/stats")
    public RestBean<CourseHoursStats> getCourseHoursStats(@RequestParam Integer studentId) {
        try {
            System.out.println("获取学生课时统计: studentId=" + studentId);
            CourseHoursStats stats = studentService.getStudentCourseHoursStats(studentId);
            System.out.println("课时统计结果: " + stats);
            return RestBean.success(stats);
        } catch (Exception e) {
            System.err.println("获取课时统计失败: " + e.getMessage());
            e.printStackTrace();
            return RestBean.error(500, "获取课时统计失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取学生课时历史记录
     */
    @PostMapping("/course-hours/history")
    public RestBean<CourseHourHistoryResponse> getCourseHoursHistory(@RequestParam Integer studentId) {
        try {
            System.out.println("获取学生课时历史记录: studentId=" + studentId);
            CourseHourHistoryResponse history = studentService.getStudentCourseHoursHistory(studentId);
            return RestBean.success(history);
        } catch (Exception e) {
            System.err.println("获取课时历史记录失败: " + e.getMessage());
            e.printStackTrace();
            return RestBean.error(500, "获取课时历史记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取学生课程列表
     */
    @GetMapping("/courses")
    public RestBean<List<Course>> getStudentCourses(@RequestParam Integer studentId) {
        try {
            System.out.println("获取学生课程列表: studentId=" + studentId);
            List<Course> courses = studentService.getStudentCourses(studentId);
            return RestBean.success(courses);
        } catch (Exception e) {
            System.err.println("获取课程列表失败: " + e.getMessage());
            e.printStackTrace();
            return RestBean.error(500, "获取课程列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取学生特定课程的课时记录
     */
    @GetMapping("/course-hours/{courseId}")
    public RestBean<List<ClassHourRecord>> getCoursesHourRecords(
            @RequestParam Integer studentId, 
            @PathVariable Integer courseId) {
        try {
            System.out.println("获取学生课时记录: studentId=" + studentId + ", courseId=" + courseId);
            List<ClassHourRecord> records = studentService.getStudentCourseClassHourRecords(studentId, courseId);
            return RestBean.success(records);
        } catch (Exception e) {
            System.err.println("获取课时记录失败: " + e.getMessage());
            e.printStackTrace();
            return RestBean.error(500, "获取课时记录失败: " + e.getMessage());
        }
    }
} 