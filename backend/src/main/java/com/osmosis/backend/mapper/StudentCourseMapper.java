package com.osmosis.backend.mapper;

import com.osmosis.backend.entity.StudentCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentCourseMapper {
    
    /**
     * 根据学生ID查询所有课程注册信息
     */
    @Select("SELECT sc.id, sc.student_id as studentId, sc.course_id as courseId, " +
            "sc.enrollment_date as enrollDate, " +
            "COALESCE(sc.total_hours, 0) as totalHours, " +
            "COALESCE(sc.completed_hours, 0) as completedHours, " +
            "sc.status, c.course_name as courseName, u.username as teacherName " +
            "FROM student_courses sc " +
            "JOIN courses c ON sc.course_id = c.id " +
            "JOIN users u ON c.teacher_id = u.id " +
            "WHERE sc.student_id = #{studentId}")
    List<StudentCourse> findByStudentId(@Param("studentId") Integer studentId);
    
    /**
     * 根据课程ID查询所有注册该课程的学生
     */
    @Select("SELECT sc.id, sc.student_id as studentId, sc.course_id as courseId, " +
            "sc.enrollment_date as enrollDate, " +
            "COALESCE(sc.total_hours, 0) as totalHours, " +
            "COALESCE(sc.completed_hours, 0) as completedHours, " +
            "sc.status, u.username as studentName, c.course_name as courseName " +
            "FROM student_courses sc " +
            "JOIN users u ON sc.student_id = u.id " +
            "JOIN courses c ON sc.course_id = c.id " +
            "WHERE sc.course_id = #{courseId} " +
            "ORDER BY sc.enrollment_date DESC")
    List<StudentCourse> findByCourseId(@Param("courseId") Integer courseId);
    
    /**
     * 查询特定学生的特定课程注册信息
     */
    @Select("SELECT sc.id, sc.student_id as studentId, sc.course_id as courseId, " +
            "sc.enrollment_date as enrollDate, " +
            "COALESCE(sc.total_hours, 0) as totalHours, " +
            "COALESCE(sc.completed_hours, 0) as completedHours, " +
            "sc.status, c.course_name as courseName, u.username as studentName, t.username as teacherName " +
            "FROM student_courses sc " +
            "JOIN courses c ON sc.course_id = c.id " +
            "JOIN users u ON sc.student_id = u.id " +
            "JOIN users t ON c.teacher_id = t.id " +
            "WHERE sc.student_id = #{studentId} AND sc.course_id = #{courseId}")
    StudentCourse findByStudentIdAndCourseId(
            @Param("studentId") Integer studentId, 
            @Param("courseId") Integer courseId);

    /**
     * 根据教师ID查询所有学生选课信息
     */
    @Select("SELECT sc.id, sc.student_id as studentId, sc.course_id as courseId, " +
            "sc.enrollment_date as enrollDate, " +
            "COALESCE(sc.total_hours, 0) as totalHours, " +
            "COALESCE(sc.completed_hours, 0) as completedHours, " +
            "sc.status, c.course_name as courseName, u.username as studentName " +
            "FROM student_courses sc " +
            "JOIN courses c ON sc.course_id = c.id " +
            "JOIN users u ON sc.student_id = u.id " +
            "WHERE c.teacher_id = #{teacherId}")
    List<StudentCourse> findByTeacherId(@Param("teacherId") Integer teacherId);
} 