package com.osmosis.backend.mapper;

import com.osmosis.backend.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper {
    
    /**
     * 根据ID查询课程
     */
    @Select("SELECT c.id, c.course_name as courseName, c.course_code as courseCode, " +
            "c.description, c.teacher_id as teacherId, " +
            "c.created_at as createdAt, c.updated_at as updatedAt, " +
            "u.username as teacherName " +
            "FROM courses c " +
            "LEFT JOIN users u ON c.teacher_id = u.id " +
            "WHERE c.id = #{id}")
    Course findCourseById(@Param("id") Integer id);
    
    /**
     * 获取学生的选课列表
     */
    @Select("SELECT c.id, c.course_name as courseName, c.course_code as courseCode, " +
            "c.description, c.teacher_id as teacherId, " +
            "c.created_at as createdAt, c.updated_at as updatedAt, " +
            "u.username as teacherName, " +
            "COALESCE(sc.total_hours, 0) as totalHours, " +
            "COALESCE(sc.completed_hours, 0) as completedHours " +
            "FROM courses c " +
            "JOIN student_courses sc ON c.id = sc.course_id " +
            "LEFT JOIN users u ON c.teacher_id = u.id " +
            "WHERE sc.student_id = #{studentId} AND sc.status = 'enrolled'")
    List<Course> findCoursesByStudentId(@Param("studentId") Integer studentId);
} 