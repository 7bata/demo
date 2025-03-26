package com.osmosis.backend.mapper;

import com.osmosis.backend.entity.ClassHourRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassHourRecordMapper {
    
    /**
     * 获取学生的课时记录
     */
    @Select("SELECT chr.*, c.course_name as courseName, s.username as studentName, t.username as teacherName " +
            "FROM class_hour_records chr " +
            "JOIN courses c ON chr.course_id = c.id " +
            "JOIN users s ON chr.student_id = s.id " +
            "JOIN users t ON chr.teacher_id = t.id " +
            "WHERE chr.student_id = #{studentId} " +
            "ORDER BY chr.class_date DESC")
    List<ClassHourRecord> findRecordsByStudentId(@Param("studentId") Integer studentId);
    
    /**
     * 获取学生在特定课程的课时记录
     */
    @Select("SELECT chr.*, c.course_name as courseName, s.username as studentName, t.username as teacherName " +
            "FROM class_hour_records chr " +
            "JOIN courses c ON chr.course_id = c.id " +
            "JOIN users s ON chr.student_id = s.id " +
            "JOIN users t ON chr.teacher_id = t.id " +
            "WHERE chr.student_id = #{studentId} AND chr.course_id = #{courseId} " +
            "ORDER BY chr.class_date DESC")
    List<ClassHourRecord> findRecordsByStudentIdAndCourseId(
            @Param("studentId") Integer studentId, 
            @Param("courseId") Integer courseId);
} 