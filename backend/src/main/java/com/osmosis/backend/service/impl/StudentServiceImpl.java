package com.osmosis.backend.service.impl;

import com.osmosis.backend.entity.*;
import com.osmosis.backend.mapper.*;
import com.osmosis.backend.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    
    @Resource
    private CourseMapper courseMapper;
    
    @Resource
    private CourseScheduleMapper courseScheduleMapper;
    
    @Resource
    private ClassHourRecordMapper classHourRecordMapper;
    
    @Resource
    private StudentCourseMapper studentCourseMapper;
    
    @Override
    public List<Course> getStudentCourses(Integer studentId) {
        return courseMapper.findCoursesByStudentId(studentId);
    }
    
    @Override
    public List<CourseSchedule> getStudentSchedule(Integer studentId) {
        return courseScheduleMapper.findSchedulesByStudentId(studentId);
    }
    
    @Override
    public Course getCourseDetail(Integer courseId) {
        return courseMapper.findCourseById(courseId);
    }
    
    @Override
    public List<CourseSchedule> getCourseSchedules(Integer courseId) {
        return courseScheduleMapper.findSchedulesByCourseId(courseId);
    }
    
    @Override
    public List<ClassHourRecord> getStudentClassHourRecords(Integer studentId) {
        return classHourRecordMapper.findRecordsByStudentId(studentId);
    }
    
    @Override
    public List<ClassHourRecord> getStudentCourseClassHourRecords(Integer studentId, Integer courseId) {
        return classHourRecordMapper.findRecordsByStudentIdAndCourseId(studentId, courseId);
    }
    
    @Override
    public List<StudentAttendance> getStudentAttendance(Integer studentId) {
        // 暂未实现
        return null;
    }
    
    @Override
    public List<StudentAttendance> getStudentCourseAttendance(Integer studentId, Integer courseId) {
        // 暂未实现
        return null;
    }
    
    @Override
    public List<CourseMaterial> getCourseMaterials(Integer courseId) {
        // 暂未实现
        return null;
    }
    
    @Override
    public List<Notification> getStudentNotifications(Integer studentId) {
        // 暂未实现
        return null;
    }
    
    @Override
    public List<Notification> getStudentUnreadNotifications(Integer studentId) {
        // 暂未实现
        return null;
    }
    
    @Override
    public boolean markNotificationAsRead(Integer notificationId, Integer studentId) {
        // 暂未实现
        return false;
    }
    
    @Override
    public CourseHoursStats getStudentCourseHoursStats(Integer studentId) {
        try {
            // 计算学生的课时统计
            System.out.println("开始获取学生ID为" + studentId + "的课时统计");
            List<StudentCourse> courses = studentCourseMapper.findByStudentId(studentId);
            
            if (courses == null) {
                System.out.println("学生课程列表为null");
                return new CourseHoursStats(0, 0);
            }
            
            System.out.println("学生课程数量: " + courses.size());
            for (StudentCourse course : courses) {
                System.out.println("课程ID: " + course.getCourseId() 
                    + ", 课程名: " + course.getCourseName()
                    + ", 总课时: " + course.getTotalHours()
                    + ", 已完成课时: " + course.getCompletedHours()
                    + ", 状态: " + course.getStatus());
            }
            
            int totalHours = 0;
            int completedHours = 0;
            
            for (StudentCourse course : courses) {
                if (course.getTotalHours() != null) {
                    totalHours += course.getTotalHours();
                }
                
                if (course.getCompletedHours() != null) {
                    completedHours += course.getCompletedHours();
                }
            }
            
            CourseHoursStats stats = new CourseHoursStats();
            stats.setRemainingHours(totalHours - completedHours);
            stats.setConsumedHours(completedHours);
            
            System.out.println("计算结果 - 总课时: " + totalHours 
                + ", 已消耗: " + completedHours 
                + ", 剩余: " + (totalHours - completedHours));
            
            return stats;
        } catch (Exception e) {
            // 记录异常信息
            System.err.println("获取课时统计失败: " + e.getMessage());
            e.printStackTrace();
            
            // 返回默认值，避免前端显示错误
            return new CourseHoursStats(0, 0);
        }
    }
    
    @Override
    public CourseHourHistoryResponse getStudentCourseHoursHistory(Integer studentId) {
        try {
            List<ClassHourRecord> records = classHourRecordMapper.findRecordsByStudentId(studentId);
            
            if (records == null) {
                // 返回空结果
                return new CourseHourHistoryResponse(new ArrayList<>(), 0);
            }
            
            List<CourseHourHistory> historyRecords = new ArrayList<>();
            
            // 将ClassHourRecord转换为CourseHourHistory
            for (ClassHourRecord record : records) {
                try {
                    CourseHourHistory history = new CourseHourHistory();
                    history.setId(record.getId());
                    
                    // 安全地解析日期
                    if (record.getClassDate() != null) {
                        history.setDate(LocalDate.parse(record.getClassDate().toString()));
                    } else {
                        history.setDate(LocalDate.now());
                    }
                    
                    history.setSubject(record.getCourseName());
                    history.setTeacher(record.getTeacherName());
                    // 假设课时为2小时
                    history.setHours(2.0);
                    history.setReport(record.getContent());
                    
                    historyRecords.add(history);
                } catch (Exception e) {
                    // 跳过有问题的记录，继续处理下一条
                    System.err.println("处理课时记录时出错: " + e.getMessage());
                }
            }
            
            CourseHourHistoryResponse response = new CourseHourHistoryResponse();
            response.setRecords(historyRecords);
            response.setTotal(historyRecords.size());
            
            return response;
        } catch (Exception e) {
            // 记录异常信息
            System.err.println("获取课时历史记录失败: " + e.getMessage());
            e.printStackTrace();
            
            // 返回空结果，避免前端显示错误
            return new CourseHourHistoryResponse(new ArrayList<>(), 0);
        }
    }
} 