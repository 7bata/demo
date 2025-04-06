package com.osmosis.backend.service.impl;

import com.osmosis.backend.entity.*;
import com.osmosis.backend.mapper.*;
import com.osmosis.backend.service.TeacherService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    
    @Resource
    private CourseMapper courseMapper;
    
    @Resource
    private CourseScheduleMapper courseScheduleMapper;
    
    @Resource
    private ClassHourRecordMapper classHourRecordMapper;
    
    @Resource
    private StudentCourseMapper studentCourseMapper;
    
    @Override
    public List<StudentCourse> getTeacherCourses(Integer teacherId) {
        try {
            // 获取教师教授的课程对应的学生选课记录
            List<StudentCourse> courses = studentCourseMapper.findByTeacherId(teacherId);
            
            if (courses == null) {
                return new ArrayList<>();
            }
            
            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<CourseSchedule> getTeacherSchedule(Integer teacherId) {
        try {
            // 获取教师的课程安排
            List<CourseSchedule> schedules = courseScheduleMapper.findSchedulesByTeacherId(teacherId);
            
            if (schedules == null) {
                return new ArrayList<>();
            }
            
            return schedules;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<StudentCourse> getCourseStudents(Integer courseId) {
        try {
            // 获取课程的学生列表
            List<StudentCourse> students = studentCourseMapper.findByCourseId(courseId);
            
            if (students == null) {
                return new ArrayList<>();
            }
            
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<ClassHourRecord> getStudentCourseClassHourRecords(Integer studentId, Integer courseId) {
        try {
            // 获取学生特定课程的课时记录
            List<ClassHourRecord> records = classHourRecordMapper.findRecordsByStudentIdAndCourseId(studentId, courseId);
            
            if (records == null) {
                return new ArrayList<>();
            }
            
            return records;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public Course getCourseDetail(Integer courseId) {
        try {
            // 获取课程详情
            return courseMapper.findCourseById(courseId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean saveClassHourRecord(ClassHourRecord record) {
        try {
            System.out.println("保存课时记录: " + record);
            int result = classHourRecordMapper.insertClassHourRecord(record);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateClassHourRecord(ClassHourRecord record) {
        try {
            System.out.println("更新课时记录: " + record);
            int result = classHourRecordMapper.updateClassHourRecord(record);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteClassHourRecord(Integer recordId) {
        try {
            System.out.println("删除课时记录ID: " + recordId);
            int result = classHourRecordMapper.deleteClassHourRecord(recordId);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public ClassHourRecord getClassHourRecordById(Integer recordId) {
        try {
            System.out.println("获取课时记录ID: " + recordId);
            return classHourRecordMapper.findRecordById(recordId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 