package com.osmosis.backend.controller;

import com.osmosis.backend.entity.*;
import com.osmosis.backend.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    public RestBean<List<CourseHourHistory>> getCoursesHourRecords(
            @RequestParam Integer studentId, 
            @PathVariable Integer courseId) {
        try {
            System.out.println("获取学生课时记录: studentId=" + studentId + ", courseId=" + courseId);
            List<ClassHourRecord> records = studentService.getStudentCourseClassHourRecords(studentId, courseId);
            
            // 记录日志
            System.out.println("查询到课时记录数: " + (records != null ? records.size() : 0));
            if (records != null && !records.isEmpty()) {
                for (ClassHourRecord record : records) {
                    System.out.println("记录ID: " + record.getId() 
                        + ", 课程名: " + record.getCourseName()
                        + ", 教师: " + record.getTeacherName()
                        + ", 日期: " + record.getClassDate()
                        + ", 状态: " + record.getStatus());
                }
            }
            
            // 转换为CourseHourHistory对象
            List<CourseHourHistory> historyRecords = new ArrayList<>();
            for (ClassHourRecord record : records) {
                CourseHourHistory history = new CourseHourHistory();
                history.setId(record.getId());
                
                // 明确处理日期：如果日期为null，使用2023年的默认日期
                LocalDate recordDate = record.getClassDate();
                if (recordDate != null) {
                    history.setDate(recordDate);
                    // 直接使用数据库中的日期生成格式化字符串
                    history.setFormattedDate(recordDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    System.out.println("记录ID: " + record.getId() + " 设置日期为: " + recordDate + ", 格式化为: " + history.getFormattedDate());
                } else {
                    // 如果日期为null，使用2023年9月15日作为默认日期（根据数据库图片中的示例日期）
                    LocalDate defaultDate = LocalDate.of(2023, 9, 15);
                    history.setDate(defaultDate);
                    history.setFormattedDate(defaultDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    System.out.println("记录ID: " + record.getId() + " 日期为null，使用默认日期: " + defaultDate);
                }
                
                history.setSubject(record.getCourseName());
                history.setTeacher(record.getTeacherName());
                history.setHours(2.0); // 默认2小时
                history.setReport(record.getContent());
                history.setStatus(record.getStatus() != null ? record.getStatus() : "completed");
                history.setStartTime(LocalTime.of(8, 0)); // 默认8:00开始
                history.setEndTime(LocalTime.of(10, 0));  // 默认10:00结束
                history.setHasReport(record.getContent() != null && !record.getContent().isEmpty());
                
                historyRecords.add(history);
            }
            
            System.out.println("转换后的历史记录数: " + historyRecords.size());
            for (CourseHourHistory history : historyRecords) {
                System.out.println("历史记录ID: " + history.getId() 
                    + ", 科目: " + history.getSubject()
                    + ", 教师: " + history.getTeacher()
                    + ", 日期: " + history.getDate()
                    + ", 状态: " + history.getStatus());
            }
            
            return RestBean.success(historyRecords);
        } catch (Exception e) {
            System.err.println("获取课时记录失败: " + e.getMessage());
            e.printStackTrace();
            return RestBean.error(500, "获取课时记录失败: " + e.getMessage());
        }
    }
} 