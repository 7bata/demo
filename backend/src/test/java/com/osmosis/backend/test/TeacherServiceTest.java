package com.osmosis.backend.test;

import com.osmosis.backend.entity.*;
import com.osmosis.backend.mapper.*;
import com.osmosis.backend.service.TeacherService;
import com.osmosis.backend.service.impl.TeacherServiceImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * TeacherService测试类
 */
public class TeacherServiceTest {
    
    /**
     * 简单测试TeacherService的实现
     */
    @Test
    public void testTeacherService() throws Exception {
        System.out.println("Testing TeacherServiceImpl...");
        
        // 创建TeacherService实例
        TeacherServiceImpl teacherService = new TeacherServiceImpl();
        
        // 设置模拟的依赖对象
        setPrivateField(teacherService, "courseMapper", new MockCourseMapper());
        setPrivateField(teacherService, "courseScheduleMapper", new MockCourseScheduleMapper());
        setPrivateField(teacherService, "classHourRecordMapper", new MockClassHourRecordMapper());
        setPrivateField(teacherService, "studentCourseMapper", new MockStudentCourseMapper());
        
        // 测试获取教师课程
        List<StudentCourse> courses = teacherService.getTeacherCourses(1);
        System.out.println("Teacher courses: " + courses.size());
        
        // 测试获取课程学生
        List<StudentCourse> students = teacherService.getCourseStudents(1);
        System.out.println("Course students: " + students.size());
        
        // 测试获取课程详情
        Course course = teacherService.getCourseDetail(1);
        System.out.println("Course detail: " + (course != null ? course.getCourseName() : "null"));
        
        // 测试获取课时记录
        List<ClassHourRecord> records = teacherService.getStudentCourseClassHourRecords(1, 1);
        System.out.println("Class hour records: " + records.size());
        
        System.out.println("TeacherServiceImpl tests completed successfully.");
    }
    
    /**
     * 通过反射设置私有字段的值
     */
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
    
    /**
     * 模拟的CourseMapper实现
     */
    class MockCourseMapper implements CourseMapper {
        @Override
        public Course findCourseById(Integer id) {
            Course course = new Course();
            course.setId(id);
            course.setCourseName("测试课程");
            course.setCourseCode("TEST001");
            course.setTeacherId(1);
            course.setTeacherName("测试教师");
            return course;
        }
        
        @Override
        public List<Course> findCoursesByStudentId(Integer studentId) {
            List<Course> courses = new ArrayList<>();
            Course course = new Course();
            course.setId(1);
            course.setCourseName("测试课程");
            course.setCourseCode("TEST001");
            course.setTeacherId(1);
            course.setTeacherName("测试教师");
            courses.add(course);
            return courses;
        }
    }
    
    /**
     * 模拟的CourseScheduleMapper实现
     */
    class MockCourseScheduleMapper implements CourseScheduleMapper {
        @Override
        public List<CourseSchedule> findSchedulesByCourseId(Integer courseId) {
            return new ArrayList<>();
        }
        
        @Override
        public List<CourseSchedule> findSchedulesByStudentId(Integer studentId) {
            return new ArrayList<>();
        }
        
        @Override
        public List<CourseSchedule> findSchedulesByTeacherId(Integer teacherId) {
            return new ArrayList<>();
        }
    }
    
    /**
     * 模拟的ClassHourRecordMapper实现
     */
    class MockClassHourRecordMapper implements ClassHourRecordMapper {
        @Override
        public List<ClassHourRecord> findRecordsByStudentId(Integer studentId) {
            return new ArrayList<>();
        }
        
        @Override
        public List<ClassHourRecord> findRecordsByStudentIdAndCourseId(Integer studentId, Integer courseId) {
            return new ArrayList<>();
        }
    }
    
    /**
     * 模拟的StudentCourseMapper实现
     */
    class MockStudentCourseMapper implements StudentCourseMapper {
        @Override
        public List<StudentCourse> findByStudentId(Integer studentId) {
            return new ArrayList<>();
        }
        
        @Override
        public List<StudentCourse> findByCourseId(Integer courseId) {
            List<StudentCourse> students = new ArrayList<>();
            StudentCourse sc = new StudentCourse();
            sc.setId(1);
            sc.setStudentId(1);
            sc.setCourseId(courseId);
            sc.setStudentName("测试学生");
            sc.setCourseName("测试课程");
            students.add(sc);
            return students;
        }
        
        @Override
        public StudentCourse findByStudentIdAndCourseId(Integer studentId, Integer courseId) {
            StudentCourse sc = new StudentCourse();
            sc.setId(1);
            sc.setStudentId(studentId);
            sc.setCourseId(courseId);
            sc.setStudentName("测试学生");
            sc.setCourseName("测试课程");
            return sc;
        }
        
        @Override
        public List<StudentCourse> findByTeacherId(Integer teacherId) {
            List<StudentCourse> courses = new ArrayList<>();
            StudentCourse sc = new StudentCourse();
            sc.setId(1);
            sc.setStudentId(1);
            sc.setCourseId(1);
            sc.setStudentName("测试学生");
            sc.setCourseName("测试课程");
            sc.setTeacherName("测试教师");
            courses.add(sc);
            return courses;
        }
    }
} 